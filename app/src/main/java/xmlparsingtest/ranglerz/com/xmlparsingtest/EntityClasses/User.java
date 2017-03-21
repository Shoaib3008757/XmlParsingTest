package xmlparsingtest.ranglerz.com.xmlparsingtest.EntityClasses;

/**
 * Created by User-10 on 04-Oct-16.
 */
public class User {
    int uId;
    String uName;
    String uPassword;
    String uEmail;
    int uContactNo;
    String uAddress;

    public String getuAddress() {
        return uAddress;
    }

    public void setuAddress(String uAddress) {
        this.uAddress = uAddress;
    }

    public int getuContactNo() {
        return uContactNo;
    }

    public void setuContactNo(int uContactNo) {
        this.uContactNo = uContactNo;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuPassword() {
        return uPassword;
    }

    public void setuPassword(String uPassword) {
        this.uPassword = uPassword;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }


}
