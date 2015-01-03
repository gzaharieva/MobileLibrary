package com.master.univt.services;

/**
 * 
 * @author Gabriela Zaharieva
 */
public class RequestResponse
{
  private int statusCode;
  private String result;

  public RequestResponse()
  {
  }

  public RequestResponse(final int statusCode, final String result)
  {
    super();
    this.statusCode = statusCode;
    this.result = result;
  }

  /**
   * @return the statusCode
   */
  public int getStatusCode()
  {
    return statusCode;
  }

  /**
   * @param statusCode the statusCode to set
   */
  public void setStatusCode(final int statusCode)
  {
    this.statusCode = statusCode;
  }

  /**
   * @return the result
   */
  public String getResult()
  {
    return result;
  }

  /**
   * @param result the result to set
   */
  public void setResult(final String result)
  {
    this.result = result;
  }

}
