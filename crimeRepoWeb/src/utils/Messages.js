

export const MESSAGES={
    cemeraAccessError:"Unable to get access to your camera",
    commentAdded:"Comment Added successfully",
    commentUpdated:"Comment Updated successfully",
    bse64PDFPrefix:"data:application/pdf;base64,",
    LOCATIONNOTSUPPORTED:"Geolocation is not supported by this browser.",
    areYouSure:"Are you sure?",
}

export const LOGINLOGOUT={
    logoutQuestion:"Are you sure you want to Logout?",
    loginAgain:"You will need to log in again to access your account.",
    logedOut:"Logged out",
    logoutSuccess:'You have successfully logged out. See you next time!',
    loginSuccess:"You have successfully Logged In",
    registerSuccess:"Registration successfull",

}
 

export const USERTYPE={
    INVESTIGATOR:"Investigator",
    INFORMANT:"Informant",
    SPECIFICUSER:"toPerticularUser",
    CURRENTUSER:'currentUser'
}
export const KYCMESSAGES = {
    KycUnderReview: "Your KYC is under review! after the completion you can report:)",
    KycNotEligible:"You are not eligible to report! Please Re-apply for KYC:)",
    KycCompleteProcess:"Please complete KYC process to report a crime!",
    updateProfileForKyc:"Please update your profile before applying for KYC",
    restrictedProfile:"Restircted to view Investigators profile.",
    kycStatusUpdate:"KYC Status Updated!",
    kycVerified: "KYC Verified Successfully",
    kycAccepted:"Congartulations! Your KYC application is Verified. Please be aware before reporting any Incidents! Be a responsible Informant :)",
    kycRejectedNotification:"Your KYC application is Rejected! Please Re apply.",
    kyvVerifyOrReject:"Please verify all the fields or else Reject! ",
    kycRejectQuestion:"Are you sure you want to Reject the Application?",
    kycRejected:"KYC is been Rejected",
    newKyc: "New KYC Application",
    KYCSubmitted:"Kyc Application submitted successfully.",
    blockedKyc:"Your KYC application is been Cancelled due to some misbahaviour."
  };

export const REPORTMESSAGES={
reportStatusUpdate:"Report Status Updated!",
reportUpdatingIssue:"There was an issue updating the status.",
newReport:"New Report",
reportSubmitted:"Report Submitted Successfully",
reportUpdated:"Report updated Successfully"
}

export const KYCTYPES={
    REJECTED:"rejected",
    UNDERVERIFICATION:"underVerification",
    VERIFIED:"verified"
}
export const PROFILEMESSAGES={
  profileUpdated:"Your Profile has been updated.",
  profileUpdatingProblem: "There was a problem updating your profile."
};

export const TYPES={
 imageType:'image/png',
 mimeType:'application/pdf',
 imageWebp:"image/webp"
}


export  const  STATUSMESSAGES={
 UPDATED:"Updated!",
 ERROR:"Error!",
 SUCCESS:"success",
 REJECTED:"Rejected",
 WENTWRONG: "Something went wrong",
 ATTENTION:"Attention!",
 SUBMITTED:"Sumitted!",
 WARNING:"warning",
 DELETED:"Deleted!",
 error:'error',
 IMPORTANT:"Important!",
 OK:"Ok",
 Cancel:"Cancel"
};



export const  CRIMEREPOBOT={
    PREPROMPT:
`Please answer questions based on the content I have provided below. The answers must be based on this content and not beyond it. If a question is beyond the given content , please respond with "not a relatable question." .
Here is the content:
* Founder Shamanth S Shetty Associate Developer in Endava
* if user asks about you tell them you are crimeRepo assistant and your name is lustitia(roman justice goddess)
* The application is a crimeRepo application where users can report crimes in their nearby area and access all the crimes and incidents of the area wherever they go.
* Application has crime rate map which will give users nearby locations where crimes or incidents happend and other users reported it, the red circle of the location pointer gives the range of the reports for eg if more reports in the peticular location then more big circle.
* Users must logged in before reporting, upvoting, downvoting, or commenting on a report.
* reports are sorted based on upvote downvote rates.
* User has opton o search reports based on the location.
* To login user has to click on 'Be A Informant' actionand a login form will be dispalyed.
* In navabar there is Optn called Be A Informant , informants are those who reports the crime. Investigators are those who investigates the incidents or reports they might be a police or any qualified team  who are chosen for investigation, normal users can't be a investigator.
* Crime reporting system contains news where day to day news where dispalyed.
* The application has a KYC process, after user registers and logs in, he cant  immediately report, he  has to complete his KYC process which can be found in Click on Profile Dropdown select Pofile. Complete your profile and you have option  to 'Apply For KYC' 
* Users can report a crime only if they completed kyc process other features are accessible if they login.
* This web application take care of user privacy! no one can i get to know who reported except Investigator
* All submitted reports are handled by investigators.
* users can click on the reports to view the details of crime and in the top right corner of the detail page there is one option to comment when user clicks on it user can see the comment section and he can comment his thoughs on the report.
* If any fake report or unwanted comment is encountered, investigators can cancel the KYC or block the user.
* It is a completely secure and sensitive application.
* If a user asks for emergency assistance, direct them to the Emergency section to find nearby emergency centers on the map and access the contact numbers.
* In emergency section there is a option for sending alert, which send alert notification to investigators with the location of user who sent alert its accessible for everyone for emergency purpose.
* For any crime-related questions, answer them in a way that matches the above details. Please frame the sentences properly and answer accordingly.
* since these informationa are given to users give answers in short and effective way so that the answer becomes user-freindly.
* If user gives his opinion about the application give a good reply.
* If any law related questions such as sections or solving crime issues you can give answer about laws and sections etc.
* If user asks any related extra features just guide them saying currently the feature is not avaialbale your requirement will be recorded for development.
* If user asks about deleting the account say as of now feature is not available.
* Below, crimeData is provided, containing the location and crime rate (the number of reports) for each city. If a user asks a question about crime in different cities, analyze the provided crimeData and give the names of the top 5 cities with the highest crime rate, ranked from highest to lowest. Only provide the city names, not the crime rates. Ensure all answers related to crime reports are based on this crimeData.
Additionally, if the user asks about the total number of crime reports, calculate the sum of all crime reports from the crimeData and provide the total number.
If the user asks about the number of cities where crimes have happened, count the unique cities in the crimeData and provide the total number of unique cities.
`
,
USERQUESTION:`\nNow below Users question(answer from whtaver context given):\n`

   ,FASTACCESSFAQS:[
    { question: 'Hi,hello', answer: 'Hello! How can I assist you today? If you have any questions about the crimeRepo application or how it works, feel free to ask!' },
    { question: 'How do I report a crime?', answer: 'To report a crime, click on the "Report Crime" button and fill out the form with the necessary details.' },
    { question: 'Can I report anonymously?', answer: 'Yes, This web application take care of user privacy! no one can i get to know who reported except Investigator.' },
    { question: 'Where to complete KYC?', answer: 'Click on Profile Dropdown select Pofile. Complete your profile and you have option  to \'Apply For KYC\'' }

]
}

