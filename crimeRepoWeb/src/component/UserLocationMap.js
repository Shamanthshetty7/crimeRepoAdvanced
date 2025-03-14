import React from 'react'
import { MapContainer, Marker,  TileLayer } from 'react-leaflet'
import 'leaflet/dist/leaflet.css';
import L from 'leaflet'
import style from "../styles/CustomStyles.module.css";
const UserLocationMap = ({position}) => {
  const { REACT_APP_STREAMMAPURL } = process.env;
  const locationMarker = "/images/locationMarker.png";
  const markerShadow="/images/markerShadow.png";
  const customIcon = new L.Icon({
          iconUrl: locationMarker, 
          iconSize: [25, 41], 
          iconAnchor: [12, 41], 
          popupAnchor: [1, -34], 
          shadowUrl: markerShadow,
          shadowSize: [41, 41]
        });
  return (
    <div  className={`m-1 ${style.mapDisplay}`}>
    <MapContainer center={position} zoom={13} scrollWheelZoom={false} className={`${style.locationMapContainer}`}>
    <TileLayer
      url={REACT_APP_STREAMMAPURL}
    />
    <Marker position={position} icon={customIcon}>
    </Marker>
  </MapContainer>
  </div>
  )
}

export default UserLocationMap