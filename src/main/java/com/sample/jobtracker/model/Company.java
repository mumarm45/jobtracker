package com.sample.jobtracker.model;

public class Company {

  private String name;
  private String url;
  private String location;

  public Company() {}

  public Company(String name, String url, String location) {
    this.name = name;
    this.url = url;
    this.location = location;
  }

  // Getters and Setters (fix the parameter names!)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {

    this.url = url;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  @Override
  public String toString() {
    return "Company{" +
            "name='" + name + '\'' +
            ", url='" + url + '\'' +
            ", location='" + location + '\'' +
            '}';
  }
}