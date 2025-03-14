import React, { useContext, useEffect, useState } from "react";
import { MapContainer, TileLayer, Marker, Popup, useMap, CircleMarker } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import L from "leaflet";
import {  getNearbyEmergencyServices } from "../service/ExternalApiService"; // Import the API call for emergency services
import style from "../styles/CustomStyles.module.css";
import AuthContext from "../store/AuthContext";

const EmergencyCentersMap = ({ setIsMapLoading}) => {
  const userLocationMarker = "/images/userIcon2.png";
  const markerShadow = "/images/markerShadow.png";

  
  const { REACT_APP_STREAMMAPURL } = process.env;
  
  const [userCurrentLocation, setUserCurrentLocation] = useState({ lat: 0, long: 0 })
  const [emergencyServices, setEmergencyServices] = useState([ ])
  const context=useContext(AuthContext)

  const userIcon = new L.Icon({
    iconUrl: userLocationMarker,
    iconSize: [50, 50],
    iconAnchor: [25, 25],
    popupAnchor: [0, -20],
    shadowUrl: markerShadow,
    shadowSize: [30, 30],
  });

  const getIconForServiceType = (type) => {
    
    switch (type.toLowerCase()) {
      case "hospital":
        return new L.Icon({
          iconUrl: "/images/hospitalIcon1.png", 
          iconSize: [25, 25],
          iconAnchor: [20, 40],
          popupAnchor: [0, -40],
          shadowUrl: "/images/markerShadow.png",
          shadowSize: [40, 40],
        });
      case "police":
        return new L.Icon({
          iconUrl: "/images/policeStationIcon1.png", 
          iconSize: [40, 40],
          iconAnchor: [20, 40],
          popupAnchor: [0, -40],
          shadowUrl: "/images/markerShadow.png",
          shadowSize: [40, 40],
        });
      case "fire":
        return new L.Icon({
          iconUrl: "/images/fireIcon.png", 
          iconSize: [40, 40],
          iconAnchor: [20, 40],
          popupAnchor: [0, -40],
          shadowUrl: "/images/markerShadow.png",
          shadowSize: [40, 40],
        });
      default:
        return new L.Icon({
          iconUrl: "/images/defaultIcon.png",
          iconSize: [40, 40],
          iconAnchor: [20, 40],
          popupAnchor: [0, -40],
          shadowUrl: "/images/markerShadow.png",
          shadowSize: [40, 40],
        });
    }
  };

  // Fetch emergency services near the user's location
  const fetchEmergencyServices = async () => {
    if (userCurrentLocation?.lat && userCurrentLocation?.long) {
      const response = await getNearbyEmergencyServices(
        userCurrentLocation.lat,
        userCurrentLocation.long
      );
      if (response.status) {
        setIsMapLoading(false);
      
        setEmergencyServices(response?.data.elements)
      } else {
        console.error("Failed to fetch emergency services:", response.message)
      }
    }
  };

  useEffect(()=>{
    const fetchLatLong=async()=>{
       await context.fetchLatLong()
      }
      fetchLatLong()
  },[])

 
  useEffect(() => {
    
    if (context?.liveLocationLatLong) {
    const LatLong=context.liveLocationLatLong.fecthedUserLocation.split(" ")
    
      setUserCurrentLocation({lat:LatLong[0],long:LatLong[1]}); 
    }
  }, [context.liveLocationLatLong]); 


  useEffect(() => {
   
    if (userCurrentLocation?.lat && userCurrentLocation?.long) {
       
      fetchEmergencyServices(); 
    }
  }, [userCurrentLocation]);

  // Hook to update map center dynamically
  const MapCenterUpdater = ({ userCurrentLocation }) => {
    const map = useMap();
    useEffect(() => {
      if (userCurrentLocation?.lat && userCurrentLocation?.long) {
        map.setView([userCurrentLocation.lat, userCurrentLocation.long]);
      }
    }, [userCurrentLocation, map]);

    return null;
  };

  return (
    <div className={`m-3 d-flex justify-content-center ${style.mapDisplay} border shadow p-4`}>
      <MapContainer
        center={[userCurrentLocation?.lat, userCurrentLocation?.long]}
        zoom={13}
        className={`${style.mapContainer}  `}
      >
        <TileLayer url={REACT_APP_STREAMMAPURL} />

        <Marker position={[userCurrentLocation?.lat, userCurrentLocation?.long]} icon={userIcon}>
          <Popup>
            <div>
              <span>
                <strong>Your current Location</strong>
              </span>
            </div>
          </Popup>
        </Marker>
 <CircleMarker
                center={[userCurrentLocation?.lat, userCurrentLocation?.long]}
                radius={10*2 }
                color="blue"
              />
        {/* Use MapCenterUpdater to handle dynamic center update */}
        <MapCenterUpdater userCurrentLocation={userCurrentLocation} />

        {/* Display emergency service markers */}
       
        {  emergencyServices?.map((service, index) => (
          <Marker
            key={index}
            position={[service?.lat, service?.lon]}
            icon={getIconForServiceType(service?.tags?.amenity||"nothing")}
          >
            <Popup>
              <div>
                <span>
                  <strong>{service?.tags?.name}</strong>
                </span>
                <p>{service?.tags?.amenity}</p>
                <p>{service?.tags?.phone}</p>
                <p>{service?.tags?.addr?.street}</p>
              </div>
            </Popup>
          </Marker>
        ))}
      </MapContainer>
    </div>
  );
};

export default EmergencyCentersMap;
