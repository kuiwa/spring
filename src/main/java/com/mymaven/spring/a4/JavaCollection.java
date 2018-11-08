package com.mymaven.spring.a4;

import java.util.*;

public class JavaCollection {

    List addressList;
    Set addressSet;
    Map addressMap;
    Properties addressProp;

    public void setAddressList(List addressList) {
        this.addressList = addressList;
    }

    public List getAddressList() {
        System.out.println("List Elements : " + addressList);
        return addressList;
    }

    public void setAddressSet(Set addressSet) {
        this.addressSet = addressSet;
    }

    public Set getAddressSet() {
        System.out.println("Set Elements : " + addressSet);
        return addressSet;
    }

    public void setAddressMap(Map addressMap) {
        this.addressMap = addressMap;
    }

    public Map getAddressMap() {
        System.out.println("Map Elements : " + addressMap);
        return addressMap;
    }

    public void setAddressProp(Properties addressProp) {
        this.addressProp = addressProp;
    }

    public Properties getAddressProp() {
        System.out.println("Property Elements : " + addressProp);
        return addressProp;
    }
}
