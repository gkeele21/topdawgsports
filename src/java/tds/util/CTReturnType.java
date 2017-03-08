package tds.util;

import java.sql.SQLException;

public enum CTReturnType {
    SUCCESS,
    SUCCESS_WITH_WARNINGS,
    DB_ERROR("A database error has occurred. Please try again. If the problem persists, contact tech support."),
    DB_CONCURRENCY_ERROR("A database concurrency error occurred, possibly because two users tried to update the same team. Please try again."),
    UNKNOWN("An unknown error occurred. Please try again. If the problem persists, please notify Customer Support."),
    USERNAME_ALREADY_TAKEN("That username is already taken. Please choose another."),
    BRANCH_NAME_ALREADY_TAKEN("A branch with that name already exists in the FFG Partners database. " +
            "Please verify that this branch has not already been entered in."),
    EMAIL_ALREADY_TAKEN("A person with that email address already exists in the FFG Partners database. " +
            "Please verify that this person has not already been entered in."),
    LENDER_ALREADY_TAKEN("A lender with that name already exists in the FFG Partners database. " +
            "Please verify that this lender has not already been entered in."),
    OFFICE_NAME_ALREADY_TAKEN("An office with that name already exists in this branch. " +
            "Please verify that this office has not already been entered in."),
    INVALID_NOTE_CATEGORY("The note category that you specified is invalid. Please select a valid note category."),
    DUPLICATE_PRIMARY_OFFICE("A primary office already exists for this branch. " +
            "Either make this new office a non-primary office, or change the existing office to not be the primary office."),
    INSUFFICIENT_RIGHTS("You do not have sufficient user rights to perform that operation."),
    ILLEGAL_USERNAME("The Username entered contains illegal characters."),
    DELETING_PRIMARY_OFFICE("You cannot delete a Branch's primary office."),
    OFFICE_NOT_FOUND("Office not found."),
    INVALID_ARGUMENTS("An invalid argument was passed to an internal function. Please notify tech support."),
    CLAIMING_CLAIMED_DISBURSEMENT("You cannot claim that Disbursement. It already belongs to another branch."),
    INVALID_LOAN_ORIGINATOR("The Employee specified as the Loan Originator is not authorized to do loans. Please check " +
            "the Branch's Employee List to correct this problem."),
    INVALID_EMPLOYEE("An Employee entered is not valid to receive Disbursements at this time. Please check this branch's " +
            "Employee List to ensure the entered employees are all valid."),
    UPLOAD_ERROR("An error occurred uploading the file. Please ensure that the file is valid, and try again."),
    FILE_EXISTS("A file with that name already exists on the server. Either rename the file you are trying to upload, " +
            "or delete the existing Document with that filename."),
    FILE_OPERATION_FAILED("The file operation failed, possibly because you don't have the necessary permissions. Try again " +
            "or contact tech support."),
    NON_MGR_AS_BRANCH_MGR("The employee you entered for Branch Manager does not have a title of Manager."),
    INSUFFICIENT_INFO_FOR_POINT_UPLOAD("To upload the Point file, you first need to have entered the Borrower's name and " +
            "the Lender Loan No."),
    INSUFFICIENT_INFO_FOR_HUD_UPLOAD("To upload the HUD file, you first need to have entered the Borrower's name and " +
            "the Lender Loan No."),
    INVALID_CLAIM("The branch specified for claiming this Disbursement is invalid"),
    INVALID_STATE
    ;

    private String _DefaultErrorMessage;

    CTReturnType() {}

    CTReturnType(String emsg) {
        _DefaultErrorMessage = emsg;
    }

    public String getDefaultErrorMessage() { return _DefaultErrorMessage; }

    public static CTReturnType valueOf(Exception e) {
        if (e instanceof SQLException) {
            return DB_ERROR;
        }
        return UNKNOWN;
    }

}
