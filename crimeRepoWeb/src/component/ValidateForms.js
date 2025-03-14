import { USERTYPE } from "../utils/Messages";

    const ValidateForm = (user,inputData) => {
   
     
     
       const newErrors={}

       if (inputData.userAdhaarFile){
        const fileType = inputData.userAdhaarFile.type;
 
        if ( !["image/jpeg", "image/png", "application/pdf"].includes(fileType)
        ) {
          newErrors.userAdhaarFile =
            "Only image and PDF files are allowed for Aadhaar Card.";
         
        }
       }
    
        if (inputData.userEmail && !inputData.userEmail.includes('@')) {
           newErrors.email = "Email must contain '@'.";
          }
          
          if (inputData.currentCity &&( inputData.currentCity.trim().length < 3&&inputData.currentCity.trim().length >50)||inputData.reportLocation&&(inputData.reportLocation.trim().length<3&&inputData.reportLocation.trim().length>50)) {
            newErrors.currentCity = "Please enter a valid city name.";
          }
       
          
        if (inputData.userName  ) {
          if(inputData.userName.length <= 1||inputData.userFullName&&inputData.userFullName.trim()<1){
            newErrors.username = "Username must be more than 1 characters.";

          }else if(inputData.userName.length >50||inputData.userFullName&&inputData.userFullName.trim()>50){
            newErrors.username = "Username should not exceed 50 characters";

          }
        }
      
        const passwordRegex = /^(?=.*[A-Z])(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,50}$/;
        if (inputData.userPassword&&!passwordRegex.test(inputData.userPassword)) {
          
          newErrors.password = "Password must be at least 8 and atmost 50 characters long, contain at least one uppercase letter and one special character.";
        }
      
        if (inputData.userPhoneNumber && !/^\d{10}$/.test(inputData.userPhoneNumber)) {
          newErrors.phoneNumber = "Phone number must be 10 digits.";
        }

        if (inputData.userAlternativeNumber && !/^\d{10}$/.test(inputData.userAlternativeNumber)) {
          newErrors.userAlternativeNumber = "Phone number must be 10 digits.";
        }
      
        if (user!=null&&user === USERTYPE.INVESTIGATOR && !inputData.investigationCentreCode) {
            newErrors.investigationCentreCode = "Investigation Centre Code is required.";
          }

       
        if(inputData.userAge){
          if(inputData.userAge<18){
                newErrors.userAge="Below age are not alowed to proceed to create profile"
          }else if(inputData.userAge>100){
            newErrors.userAge="Age can't be more than 100"

          }
         
      }

      if (inputData.userAddress) {
        if(inputData.userAddress.length <= 10){
          newErrors.userAddress = "Address  must be more than 50 characters.";
        }else if(inputData.userAddress.length > 200){
          newErrors.userAddress = "Address  should not cross 100 characters.";
        }
      
      }

      if(inputData.userDOB){
        const today = new Date();
        const dob = new Date(inputData.userDOB);
        const age = today.getFullYear() - dob.getFullYear();
        if (age < 18) {
          newErrors.userDOB = "You must be at least 18 years old to apply.";
        }else if(dob.getFullYear()>=today.getFullYear() ){
          newErrors.userDOB = "Date of birth cant be greater than or equal to current year";

        }else if(age>100){
          newErrors.userDOB = "Date of birth can't exceed 100 years old!";

        }
      }


     
      if (inputData.reportTitle  ) {
        if(inputData.reportTitle.length <= 10){
          newErrors.reportTitle = "title must be more than 10 characters.";
        }else if(inputData.reportTitle.length >50){
          newErrors.reportTitle = "title must be less than 50 characters.";

        }
       
      }

      if (inputData.reportDescription && inputData.reportDescription.length <= 25) {
        newErrors.reportDescription = "description must be more than 25 characters.";
      }

      if (inputData.detailedInformation && inputData.detailedInformation.length <= 100) {
        newErrors.detailedInformation = "detailed Information must be more than 100 characters.";
      }


      //newsform
      if (inputData.newsHeadline && inputData.newsHeadline.length <= 25) {
        newErrors.newsHeadline = "news headline must be more than 25 characters.";
      }
      if (inputData.newsSmallDescription && inputData.newsSmallDescription.length <= 50) {
        newErrors.newsSmallDescription = "news small description must be more than 50 characters.";
      }

      if (inputData.newsDetails && inputData.newsDetails.length <= 150) {
        newErrors.newsDetails = "news Details  must be more than 150 characters.";
      }

     
      
      

  
        return newErrors;



    }
      
       
      

   
 
 
 export default ValidateForm
 