package com.master.univt.services;

/**
 * The main service for communication between application requests and Rest API.
 * 
 * @author Gabriela Zaharieva
 * 
 * @param <TResult> the result from the API call
 */
public interface CommunicationService<TResult>
{

  /**
   * The behavior when the API call is completed.
   * 
   * @param resultData the result from the ARI call
   */
  public void onRequestCompleted(final TResult resultData);
}
