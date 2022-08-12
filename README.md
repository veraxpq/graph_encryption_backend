# Steganography backend

This project is the backend implementation of the CS6760 final project. The framework of the project is 
SpringBoot + Java + MySQL.

The project is built in MVC model, the backend part contains the models and controllers. For the model 
part, there are two tables in the database, which represents two models: UserInfo and ImageInfo. Based 
on the two models, we divide the controllers into two parts: ImageController and UserController. 
ImageController contains all APIs related to ImageInfo, for example, creating and retrieving images.
UserController contains all APIs related to UserInfo, for example, creating, updating, retrieving, 
deleting users.

For security issues, we encrypted the passwords before we store them into the database. And we add token
verification to most APIs to intercept suspicious users.

## Steganographic model

<img width="633" alt="Screen Shot 2022-08-11 at 4 55 22 PM" src="https://user-images.githubusercontent.com/101501462/184261258-a3a27ab4-26bf-4d9e-bda0-88b672222ebc.png">

The steganographic encoder is a function we wrote that takes in the parameters of the cover file (an image), and a secret message that the user inputs (String), and to strengthen the security we added a key, which is a password the user enters to encrypt the secret message, the receiver will have to use the same key when decoding the secret message, that is an added layer of security. When the cover file gets sent to the steganographic encoder, which is in the EncryptLSB class in our util package of the SpringBoot class. The encryption and decryption classes are packaged into beans using the @Component annotation. After encoding, the stego object will be returned, this stego object looks exactly the same as our original file, it goes into our database, which the received cannot get without entering a password. When the stego object is sent to the decoder, the secret message goes through the decryption process whereby the changed bits of the pixels get read and returned.

## LSB Steganography Technique

<img width="678" alt="Screen Shot 2022-08-11 at 5 14 19 PM" src="https://user-images.githubusercontent.com/101501462/184262845-dcc83240-ced8-4162-a84f-1c4321463968.png">

This steganographic project uses LSB stegranographic techniques, whereby the secret message is encoded as binary and alters the least significant bits of the original RGB in the image pixels. The changes are undetectable to the human eye, but when the stego object (altered image) gets passed into the decoder (DecryptLSB class), the pixels get read back into binary and the secret message is returned. 

## Implementation

The "service" package of the java classes in the source file contains all the service layers of the project. The main service layers are the ImageService Interface and UserService Interface, which are implemented by the ImageServiceImp class and UserServiceImpl class. These classes handle the API calls that return JSON objects to the frontend that makes the API calls for backend functions, including encrypt and decrypt methods.  
The "mode" package are setters and getters for information related to login implementation.
The "util" package includes our CipherHelpers, which is an implementation of our project security, DecryptLSB and EncryptLSB classes for the steganographic implementations, HttpUtils class that send request to url, JWTUtils class for realized security, Pixel class that allows the EncryptLSB and DecryptLSB methods to manipulate pixels of the stego image object, a StatusCode class for our HTTP request server status, an Uploader class that uses a third party API to upload image files that return URLs for the EncryptLSB class to read, and WebException class under the Apache License that handles and shows http errors. 
We have a "wrapper" package with a VerifyToken class and bean that intercepts to add token verification before our backend API gets called. 

## Backend Tests

The backend tests are in the "test" folder, with unit tests for multiple ImageController methods and UserController methods, as well as integration tests for these methods.






