import { useState } from 'react';
import {
  getCurrentWeather,
  getCurrentWeatherByCoords,
  getForecast,
  getForecastByCoords
} from '../api/weatherApi';

export function useWeather() {
  const [weather, setWeather] = useState(null);
  const [forecast, setForecast] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchByCity = async (city) => {
    setLoading(true);
    setError(null);
    try {
      const [weatherData, forecastData] = await Promise.all([
        getCurrentWeather(city),
        getForecast(city)
      ]);
      setWeather(weatherData);
      setForecast(forecastData);
    } catch (err) {
      const serverMessage = err.response?.data?.message;
      setError(serverMessage || err.message || 'Failed to fetch weather data');
    } finally {
      setLoading(false);
    }
  };

  const fetchByCoords = async (lat, lon) => {
    setLoading(true);
    setError(null);
    try {
      const [weatherData, forecastData] = await Promise.all([
        getCurrentWeatherByCoords(lat, lon),
        getForecastByCoords(lat, lon)
      ]);
      setWeather(weatherData);
      setForecast(forecastData);
    } catch (err) {
      const serverMessage = err.response?.data?.message;
      setError(serverMessage || err.message || 'Failed to fetch weather data');
    } finally {
      setLoading(false);
    }
  };

  return { weather, forecast, loading, error, fetchByCity, fetchByCoords };
}
