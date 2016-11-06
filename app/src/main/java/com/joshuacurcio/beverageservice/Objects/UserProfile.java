package com.joshuacurcio.beverageservice.Objects;

/**
 * Created by sammy on 10/29/2016.
 */

public class UserProfile
{
    private String FirstName;
    private String LastName;
    private String Email;
    private String Address;
    private String PIN;
    private String Type;

    public UserProfile()
    {

    }

    public UserProfile(String FirstName, String LastName, String Email, String Address, String PIN, String Type)
    {
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Email = Email;
        this.Address = Address;
        this.PIN = PIN;
        this.Type = Type;
    }

    public String getType() {
        return Type;
    }

    public String getPIN() {
        return PIN;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public String getFirstName()
    {
        return FirstName;
    }

    public void setFirstName(String FirstName)
    {
        this.FirstName = FirstName;
    }

    public String getLastName()
    {
        return LastName;
    }

    public void setLastName(String LastName)
    {
        this.LastName = LastName;
    }

    public String getEmail()
    {
        return Email;
    }

    public void setEmail(String Email)
    {
        this.Email = Email;
    }

    public String getAddress()
    {
        return Address;
    }

    public void setAddress(String Address)
    {
        this.Address = Address;
    }

}
