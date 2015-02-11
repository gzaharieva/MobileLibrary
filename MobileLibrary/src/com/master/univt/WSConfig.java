package com.master.univt;
/**
 * Web server configurations, URLs.
 *
 * @author Gabriela Zaharieva
 */
public final class WSConfig
{

    private WSConfig()
    {
    }

    public static final String ROOT_API = "https://www.googleapis.com/books/v1/";

    /** The user login / logout services. */
    public static final String WS_BOOKSHELFS = "bookshelves";
    public static final String WS_VOLUMES = "volumes";


    public static String buildServiceURL(final String serviceURL, final String normalizedTitle)
    {
        return String.format(serviceURL, normalizedTitle);
    }
}
