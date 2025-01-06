package com.example.bankApi.User.userSettings;

import org.springframework.stereotype.Component;

@Component
public class UserSettings {

    public String getFirstName(String fullName) {
        StringBuilder firstname = new StringBuilder();

        for (int i = 0; i < fullName.length(); i++) {
            firstname.append(fullName.charAt(i));
            if(fullName.charAt(i) == ' '){
                break;
            }
        }
        firstname.setCharAt(0, Character.toUpperCase(firstname.charAt(0)));
        firstname.setLength(firstname.length()-1);

        return firstname.toString();
    }
}
