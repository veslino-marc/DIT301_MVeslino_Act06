# DIT3-1-MarcVeslino-Act06

1. Which API did you choose and why?

I chose the OpenWeatherMap API because it is beginner-friendly with clear documentation and straightforward JSON responses. It provides a free tier that gives sufficient API calls for development and testing without requiring payment. The API offers real-time weather data which demonstrates the practical application of API fetching. The JSON structure is clean and easy to parse with Kotlin data classes, and OpenWeatherMap is a widely-used and stable service with minimal downtime, making it ideal for learning REST API integration in Android.

2. How did you implement data fetching and JSON parsing?

I implemented the data fetching using Retrofit library with Gson converter for automatic JSON parsing. First, I created a RetrofitClient singleton object to manage API connections with the base URL pointing to the OpenWeatherMap API. Then I defined a WeatherApiService interface with GET annotations and Query parameters for city name, API key, and units. For the data structure, I created Kotlin data classes matching the API JSON structure using SerializedName annotations to map JSON keys to Kotlin properties. The data classes include nested objects for Main, Wind, Weather, and System information. For the actual API calls, I used Retrofit's enqueue method for asynchronous background network operations and implemented the Callback interface with onResponse and onFailure methods to handle successful responses and errors. After receiving the data, I updated the UI on the main thread to display the weather information.

3. What challenges did you face when handling errors or slow connections?

The first major challenge was network connectivity issues. The app initially crashed when there was no internet connection, so I implemented comprehensive error handling in the onFailure callback that checks for specific error messages like "Unable to resolve host" and displays user-friendly messages to the user. Another challenge was handling invalid city names. The API returns a 404 error for non-existent cities but it wasn't handled gracefully at first. I solved this by adding response code checking in the onResponse method to handle different HTTP status codes like 404 and 401 with specific error messages.

4. How would you improve your app's UI or performance in future versions?

For UI improvements, I would add weather condition icons using the icon codes from the API response to make it more visual and appealing. I would implement a RecyclerView to show a 5-day weather forecast instead of just current weather. Adding GPS functionality to automatically detect the user's location would improve user experience. I would also add a search history feature to show recently searched cities for quick access, implement dark mode theme switching, add animated backgrounds that change based on weather conditions, and design custom error layouts instead of just showing text messages.
