import axios from 'axios';

// Use relative /api (handled by Vite proxy in dev) or custom env URL if provided
const baseURL = import.meta.env.VITE_API_URL || '/api';

const api = axios.create({
  baseURL,
});

// Automatically attach JWT token if available
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('skypulse_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// --- Weather Endpoints ---
export const getCurrentWeather = async (city) => {
  const response = await api.get(`/weather/current?city=${encodeURIComponent(city)}`);
  return response.data;
};

export const getCurrentWeatherByCoords = async (lat, lon) => {
  const response = await api.get(`/weather/current?lat=${lat}&lon=${lon}`);
  return response.data;
};

export const getForecast = async (city) => {
  const response = await api.get(`/weather/forecast?city=${encodeURIComponent(city)}`);
  return response.data;
};

export const getForecastByCoords = async (lat, lon) => {
  const response = await api.get(`/weather/forecast?lat=${lat}&lon=${lon}`);
  return response.data;
};

// --- Auth Endpoints ---
export const registerUser = async (email, password) => {
  const response = await api.post('/auth/register', { email, password });
  return response.data;
};

export const loginUser = async (email, password) => {
  const response = await api.post('/auth/login', { email, password });
  return response.data;
};

export const logoutUser = async () => {
  try {
    await api.post('/auth/logout');
  } finally {
    localStorage.removeItem('skypulse_token');
    localStorage.removeItem('skypulse_user');
  }
};

// --- Saved Locations Endpoints ---
export const saveLocationByCity = async (city) => {
  const response = await api.post(`/save?city=${encodeURIComponent(city)}`);
  return response.data;
};

export const saveLocationByCoords = async (latitude, longitude) => {
  const response = await api.post(`/save?latitude=${latitude}&longitude=${longitude}`);
  return response.data;
};

export const getSavedLocations = async () => {
  const response = await api.get('/save/current');
  return response.data;
};

