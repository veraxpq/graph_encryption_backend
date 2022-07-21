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