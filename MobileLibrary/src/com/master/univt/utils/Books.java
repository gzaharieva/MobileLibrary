package com.master.univt.utils;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the course.
 * 
 * @author Gabriela Zaharieva
 */
public class Books implements Serializable
{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  @SerializedName("author")
  private String author;
  @SerializedName("productId")
  private String productId;
  @SerializedName("title")
  private String title;
  private String normalizedTitle;
  @SerializedName("image")
  private String imageURL;
  @SerializedName("parentUid")
  private String anotation;
  @SerializedName("showBuy")
  private boolean showBuy;
  @SerializedName("isFree")
  private boolean isFree;
  @SerializedName("price")
  private String price;
  private boolean isLecture;
  private double rating;

  private List<Books> courses;
  private List<Books> lectures;

  public boolean hasLectures()
  {
    return getLectures() != null && !getLectures().isEmpty();
  }

  public boolean hasCourses()
  {
    return getCourses() != null && !getCourses().isEmpty();
  }

  public boolean isInAppPurchaseEnabled()
  {
    double price = Double.parseDouble(getPrice().replace(",", ""));
    if (price < 0.5 || price > 199.00)
    {
      return false;
    }
    return true;
  }

  /**
   * @return the uID
   */
  public String getAuthor()
  {
    return author;
  }

  /**
   * @param uID the uID to set
   */
  public void setAuthor(final String uID)
  {
    this.author = uID;
  }

  /**
   * @return the productId
   */
  public String getProductId()
  {
    return productId;
  }

  /**
   * @param productId the productId to set
   */
  public void setProductId(final String productId)
  {
    this.productId = productId;
  }

  /**
   * @return the title
   */
  public String getTitle()
  {
    return title;
  }

  /**
   * 
   * @param title the title to set
   */
  public void setTitle(final String title)
  {
    this.title = title;
  }

  /**
   * @return the normalizedTitle
   */
  public String getNormalizedTitle()
  {
    return normalizedTitle;
  }

  /**
   * @param normalizedTitle the normalizedTitle to set
   */
  public void setNormalizedTitle(final String normalizedTitle)
  {
    this.normalizedTitle = normalizedTitle;
  }

  /**
   * @return the imageURL
   */
  public String getImageURL()
  {
    return imageURL;
  }

  /**
   * @param imageURL the imageURL to set
   */
  public void setImageURL(final String imageURL)
  {
    this.imageURL = imageURL;
  }

  /**
   * @return the showBuy
   */
  public boolean isShowBuy()
  {
    return showBuy;
  }

  /**
   * @param showBuy the showBuy to set
   */
  public void setShowBuy(final boolean showBuy)
  {
    this.showBuy = showBuy;
  }

  @Override
  public String toString()
  {
    return title;
  }

  public List<Books> getCourses()
  {
    return courses;
  }

  public void setCourses(final List<Books> courses)
  {
    this.courses = courses;
  }

  public List<Books> getLectures()
  {
    return lectures;
  }

  public void setLectures(final List<Books> lectures)
  {
    for (Books lecture : lectures)
    {
      lecture.setLecture(true);
    }
    this.lectures = lectures;
  }

  /**
   * @return the freeLecturesCount
   */
  public String getAnotation()
  {
    return anotation;
  }

  /**
   * @param freeLecturesCount the freeLecturesCount to set
   */
  public void setAnotation(final String anotation)
  {
    this.anotation = anotation;
  }


  /**
   * @return the isFree
   */
  public boolean isFree()
  {
    return isFree;
  }

  /**
   * @param isFree the isFree to set
   */
  public void setFree(final boolean isFree)
  {
    this.isFree = isFree;
  }

  /**
   * @return the price
   */
  public String getPrice()
  {
    return price;
  }

  public String getNormalizedPrice()
  {
    return price != null ? price.replace(".", ",") : price;
  }

  /**
   * @param price the price to set
   */
  public void setPrice(final String price)
  {
    this.price = price;
  }

  /**
   * @return the isLecture
   */
  public boolean isLecture()
  {
    return isLecture;
  }

  /**
   * @param isLecture the isLecture to set
   */
  public void setLecture(final boolean isLecture)
  {
    this.isLecture = isLecture;
  }

  public void updateLectureStatus(final Books course)
  {
    if (course.hasLectures())
    {
      for (Books lecture : course.getLectures())
      {
        lecture.setLecture(true);
      }
    }
    if (course.hasCourses())
    {
      for (Books tempCourse : course.getCourses())
      {
        {
          updateLectureStatus(tempCourse);
        }
      }
    }
  }
  
  public double getRating(){
	  return rating;
  }

}