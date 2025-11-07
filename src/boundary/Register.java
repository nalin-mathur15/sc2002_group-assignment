package boundary;
import utility.InputService;

import java.util.Map;

import entity.CompanyRepresentative;

public class Register {
    //String userID, String name, String email, String pwd, String companyname, String dep, String position
    private Map<String, CompanyRepresentative> allCompanyReps;
    public Register(Map<String, CompanyRepresentative> allCompanyReps) {
        this.allCompanyReps = allCompanyReps;
    }

    public void Start() {
        String name = InputService.readString("Enter your name: ");
        String email = InputService.readString("Enter your email: ");
        String pwd = "password";
        String companyname = InputService.readString("Enter your company name: ");
        String dep = InputService.readString("Enter your department: ");
        String position = InputService.readString("Enter your position: ");
        String userID = companyname.substring(0, Math.min(companyname.length(), 3)) 
                    + name.substring(0, Math.min(name.length(), 3))  
                    + Long.toString(System.currentTimeMillis()).substring(0,4); //generate userID based on timestamp
        
        CompanyRepresentative newCompRep = new CompanyRepresentative(userID, name, email, pwd, companyname, dep, position);
        allCompanyReps.put(userID, newCompRep);
        newCompRep.approveRepresentative(false); //needs staff approval
        System.out.println("Registration successful! Your user ID is: " + userID);
        System.out.println("Back to main menu ... ");
    }
}
