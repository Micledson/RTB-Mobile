package com.rtb.rtb.messages

class SignUpMessages {
    var emailIsNullErrorMessage = "The email address field cannot be empty!"
    var emailIsInvalidErrorMessage = "The email address field does not have a valid format!"
    var emailIsAlreadyRegisteredErrorMessage = "Email has already been registered!"

    var firstNameIsNullErrorMessage = "The first name field cannot be empty!"
    var lastNameIsNullErrorMessage = "The last name field cannot be empty!"

    var passwordIsNullErrorMessage = "The password field cannot be empty!"
    var passwordIsInvalidErrorMessage = "The password field must be at least 6 characters long!"

    var passwordConfirmationIsNullErrorMessage = "The password confirmation field cannot be empty!"
    var passwordConfirmationIsInvalidErrorMessage = "The password confirmation field must be at least 6 characters long!"

    var passwordsIsNotEqualsErrorMessage = "Password and Password confirmation must be the same!"
}