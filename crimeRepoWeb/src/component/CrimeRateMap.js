import React, { Fragment, useEffect, useState } from "react";
import {
  MapContainer,
  TileLayer,
  CircleMarker,
  Popup,
  Marker,
  useMap,
} from "react-leaflet";
import "leaflet/dist/leaflet.css";
import L from "leaflet";
import style from "../styles/CustomStyles.module.css";
import { getCoordinatesByAddresses } from "../service/ExternalApiService";

const CrimeMap = ({ context, setIsMapLoading }) => {
  const locationMarker = "/images/redLocationMarker.png";
  const markerShadow = "/images/markerShadow.png";
  const [totalCrimeCount, setTotalCrimeCount] = useState(0);
  const [highestCrimeLocation, setHighestCrimeLocation] = useState({
    lat: 0,
    long: 0,
  });

  const { REACT_APP_STREAMMAPURL } = process.env;
  const [crimeRate, setCrimerate] = useState({});
  const [crimeData, setCrimeDataValue] = useState([]);
  const [userCurrentLocation, setUserCurrentLocation] = useState({
    lat: 0,
    long: 0,
  });
  const userLocationMarker = "/images/userIcon1.png";

  //setting crimerate based on the location
  const fetchLocationSetCrimeRate = () => {
    const crimeRates = context.allReports?.reduce((acc, report) => {
      const location = report.reportLocation;
      if (acc[location]) {
        acc[location] += 1;
      } else {
        acc[location] = 1;
      }

      return acc;
    }, {});

    setCrimerate(crimeRates);
  };

  //setting crimedata
  const setCrimeData = async () => {
    if (!crimeRate) {
      setIsMapLoading(false);
      return;
    }
    const locations = Object.keys(crimeRate);
    if (locations.length != 0) {
      const response = await getLatAndLong(locations);

      const crimeData = response?.map((data) => ({
        locationName: data?.locationName,
        latitude: parseFloat(data?.latitude),
        longitude: parseFloat(data?.longitude),
        crimerate: crimeRate[data?.locationName],
      }));
      setCrimeDataValue(crimeData);
      context.setCrimeData(crimeData);
      if (crimeData.length != 0) {
        setIsMapLoading(false);
      }
    }
  };

  useEffect(() => {
    context.fetchLatLong();
  }, []);

  useEffect(() => {
    if (context.liveLocationLatLong?.fecthedUserLocation) {
      const [lat, long] =
        context.liveLocationLatLong.fecthedUserLocation.split(" ");
      setUserCurrentLocation({ lat: parseFloat(lat), long: parseFloat(long) });
    }
  }, [context.liveLocationLatLong]);

  useEffect(() => {
    setCrimeData();
  }, [crimeRate]);

  useEffect(() => {
    fetchLocationSetCrimeRate();
  }, [context.allReports]);

  //finding lat and longitude by address
  const getLatAndLong = async (address) => {
    const coordinatesWithData = await getCoordinatesByAddresses(address);
    if (coordinatesWithData.status) {
      return coordinatesWithData.data;
    } else {
      return null;
    }
  };

  const customIcon = new L.Icon({
    iconUrl: locationMarker,
    iconSize: [25, 30],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    shadowUrl: markerShadow,
    shadowSize: [41, 41],
  });

  const userIcon = new L.Icon({
    iconUrl: userLocationMarker,
    iconSize: [70, 70],
    iconAnchor: [35, 70],
    popupAnchor: [0, -70],
    shadowUrl: markerShadow,
    shadowSize: [70, 70],
  });

  const setTotalCrimeCountFunc = () => {
    let totalCrime = 0;
    let highestCrime = { lat: 0, long: 0, crimerate: 0, locationName: "" };
    crimeData.forEach((data) => {
      totalCrime += data.crimerate;
      if (data.crimerate > highestCrime.crimerate) {
        highestCrime.crimerate = data.crimerate;
        highestCrime.lat = data.latitude;
        highestCrime.long = data.longitude;
        highestCrime.locationName = data.locationName;
      }
    });

    setTotalCrimeCount(totalCrime);
    setHighestCrimeLocation(highestCrime);
  };

  useEffect(() => {
    setTotalCrimeCountFunc();
  }, [crimeData]);

  // Function to validate coordinates
  const isValidCoordinates = (lat, long) => {
   
    return !isNaN(lat) && !isNaN(long) ;
  };

  

  // Hook to update map center dynamically
  const MapCenterUpdater = ({ userCurrentLocation }) => {
    const map = useMap();
    useEffect(() => {
      if (parseFloat(userCurrentLocation.lat )&& parseFloat(userCurrentLocation.long)) {
        map.setView([parseFloat(userCurrentLocation?.lat), parseFloat(userCurrentLocation?.long)]);
      }
    }, [userCurrentLocation, map]);

    return null;
  };

  return (
    <Fragment>
      <marquee width="60%" direction="left">
        <p className="text-danger">
        {crimeData.filter(location=>isValidCoordinates(location.latitude,location.longitude)).length==0?
          (<strong>Currently map data is not avaialbale</strong>)
          
          :(
            <Fragment>
          <strong>{highestCrimeLocation.locationName} </strong>has the highest
          crime rate in the world with{" "}
          {parseInt(100 * (highestCrimeLocation.crimerate / totalCrimeCount))} %
          of crime rate
          </Fragment>
          )}
        </p>
      </marquee>
      <div className={`m-3 d-flex justify-content-center ${style.mapDisplay}`}>
        <MapContainer
          center={[userCurrentLocation?.lat, userCurrentLocation?.long]}
          zoom={13}
          className={`${style.mapContainer}`}
        >
          <TileLayer url={REACT_APP_STREAMMAPURL} />
        <Marker
            position={[parseFloat(userCurrentLocation?.lat), parseFloat(userCurrentLocation?.long)]}
            icon={userIcon}
          >
            <Popup>
              <div>
                <span>
                  <strong>Your current Location</strong>
                </span>
              </div>
            </Popup>
          </Marker>
          {/* Use MapCenterUpdater to handle dynamic center update  */}
          <MapCenterUpdater userCurrentLocation={userCurrentLocation} />
         
          {crimeData.filter(location=>isValidCoordinates(location.latitude,location.longitude)).map((location, index) => (
            
            <Marker

              key={index}
              position={[location?.latitude, location?.longitude]}
              icon={customIcon}
            >
              <Popup>
                <div>
                  <span>
                    <strong>{location.locationName}</strong>
                  </span>
                  <p>
                    Crime Rate: {parseInt(100 * (location?.crimerate / totalCrimeCount))}%
                  </p>
                </div>
              </Popup>

              <CircleMarker
                center={[parseFloat(location?.latitude),parseFloat( location?.longitude)]}
               
                radius={(parseInt(location?.crimerate) && totalCrimeCount > 0) 
                  ? 10 * (parseInt(location?.crimerate) / totalCrimeCount):0}
                color="red"
              />
            </Marker> 
         
          ))}
        </MapContainer>
      </div>
    </Fragment>
  );
};

export default CrimeMap;
