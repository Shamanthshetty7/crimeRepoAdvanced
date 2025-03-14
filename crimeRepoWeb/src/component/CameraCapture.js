import React, { useState, useRef } from 'react';
import styles from "../styles/CustomStyles.module.css";
import Swal from 'sweetalert2'
import { MESSAGES, STATUSMESSAGES, TYPES } from '../utils/Messages';
const CameraCapture = ({kycImage,setKycImage}) => {
  const [stream, setStream] = useState(null);
  const[showVideo,setShowVideo]=useState(false);
  const videoRef = useRef(null);
  const canvasRef = useRef(null);

  const startCamera = async () => {
    setShowVideo(true);
    try {
        setKycImage(null);
      const mediaStream = await navigator.mediaDevices.getUserMedia({ video: true });
      setStream(mediaStream);
      if (videoRef.current) {
        videoRef.current.srcObject = mediaStream;
      }
    } catch (error) {
      Swal.fire(STATUSMESSAGES.ERROR,MESSAGES.cemeraAccessError,STATUSMESSAGES.ERROR)
    }
  };

  const takePicture = () => {
    if (videoRef.current && canvasRef.current) {
      const context = canvasRef.current.getContext('2d');
      context.drawImage(videoRef.current, 0, 0, canvasRef.current.width, canvasRef.current.height);
      const imageData = canvasRef.current.toDataURL(TYPES.imageType);
     
      setKycImage(imageData);

    }
    stopCamera();
  };

  const stopCamera = () => {
    setShowVideo(false);
    if (stream) {
      stream.getTracks().forEach(track => track.stop());
      setStream(null);
    }
  };

  return (
    <div >
      <p><strong>Camera Capture</strong></p>
      
      <button className={styles.kycFormbtn} onClick={startCamera}>Start Camera</button>
    
      {!kycImage&& 
      <>
      
      <button className={styles.kycFormbtn} onClick={takePicture}>Take Picture</button>
      <button  className={styles.kycFormbtn} onClick={stopCamera}>Stop Camera</button>
      
     {showVideo&&( <div>
        <video ref={videoRef} autoPlay className={styles.kycImg} ></video>
      </div>)}
      </>}
      <canvas ref={canvasRef}  className={`${styles.displayNone}`}  width="500" height="500"></canvas>
      {kycImage && (
        <div>
          <p>Captured Image:</p>
          <img src={kycImage} alt="Captured" className={styles.kycImg}  />
        </div>
      )}
    </div>
  );
};

export default CameraCapture;
