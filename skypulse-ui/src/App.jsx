import React, { useEffect, useState, useCallback } from 'react';
import { SearchBar } from './components/SearchBar';
import { CurrentWeather } from './components/CurrentWeather';
import { ForecastList } from './components/ForecastList';
import { LoadingSpinner } from './components/LoadingSpinner';
import { ErrorMessage } from './components/ErrorMessage';
import { AuthModal } from './components/AuthModal';
import { SavedLocations } from './components/SavedLocations';
import { useWeather } from './hooks/useWeather';
import { useGeolocation } from './hooks/useGeolocation';
import { getWeatherBackground } from './utils/formatters';
import { logoutUser, getSavedLocations, saveLocationByCity } from './api/weatherApi';
import { MdAccountCircle, MdLogout, MdBookmark } from 'react-icons/md';

function App() {
  const { weather, forecast, loading, error, fetchByCity, fetchByCoords } = useWeather();
  const { getLocation, loading: gpsLoading } = useGeolocation();
  const [hasInitialLoad, setHasInitialLoad] = useState(false);

  // Authentication & Saved Locations State
  const [currentUser, setCurrentUser] = useState(() => localStorage.getItem('skypulse_user'));
  const [isAuthOpen, setIsAuthOpen] = useState(false);
  const [savedLocations, setSavedLocations] = useState([]);
  const [savedLoading, setSavedLoading] = useState(false);
  const [savingLocation, setSavingLocation] = useState(false);

  // Fetch saved locations for authenticated user
  const loadSavedLocations = useCallback(async () => {
    if (!localStorage.getItem('skypulse_token')) {
      setSavedLocations([]);
      return;
    }
    setSavedLoading(true);
    try {
      const data = await getSavedLocations();
      setSavedLocations(Array.isArray(data) ? data : []);
    } catch (err) {
      console.error('Failed to load saved locations:', err);
    } finally {
      setSavedLoading(false);
    }
  }, []);

  useEffect(() => {
    if (currentUser) {
      loadSavedLocations();
    }
  }, [currentUser, loadSavedLocations]);

  useEffect(() => {
    // Attempt auto-detect on mount
    const loadInitialData = async () => {
      try {
        const { lat, lon } = await getLocation();
        await fetchByCoords(lat, lon);
      } catch (err) {
        // Fallback to a default city if GPS fails or is denied
        console.log("GPS load failed or denied, using default city");
        await fetchByCity('London');
      } finally {
        setHasInitialLoad(true);
      }
    };

    loadInitialData();
  }, []);

  const handleGpsSearch = async () => {
    try {
      const { lat, lon } = await getLocation();
      fetchByCoords(lat, lon);
    } catch (err) {
      console.error(err);
    }
  };

  const handleRetry = () => {
    fetchByCity('London');
  };

  const handleAuthSuccess = (userData) => {
    setCurrentUser(userData.email);
    loadSavedLocations();
  };

  const handleLogout = async () => {
    await logoutUser();
    setCurrentUser(null);
    setSavedLocations([]);
  };

  const handleSaveCurrentCity = async (cityName) => {
    if (!cityName || !currentUser) return;
    setSavingLocation(true);
    try {
      await saveLocationByCity(cityName);
      await loadSavedLocations();
    } catch (err) {
      console.error('Failed to save location:', err);
    } finally {
      setSavingLocation(false);
    }
  };

  const isCurrentCitySaved = weather?.city && savedLocations.some(
    (loc) => loc.city && loc.city.toLowerCase() === weather.city.toLowerCase()
  );

  const bgGradient = getWeatherBackground(weather?.description);

  return (
    <div className={`min-h-screen bg-gradient-to-br ${bgGradient} transition-colors duration-1000 py-6 px-4 sm:px-6 lg:px-8`}>
      <div className="max-w-4xl mx-auto">
        {/* Top Navigation Bar */}
        <nav className="flex items-center justify-between pb-8 mb-6 border-b border-white/10">
          <div className="flex items-center space-x-2">
            <span className="text-2xl font-black tracking-tight text-white drop-shadow">
              Sky<span className="text-blue-400">Pulse</span>
            </span>
          </div>

          <div className="flex items-center space-x-3">
            {currentUser ? (
              <div className="flex items-center space-x-3 bg-white/10 backdrop-blur-md px-4 py-2 rounded-full border border-white/15">
                <MdAccountCircle className="text-blue-300" size={20} />
                <span className="text-sm font-medium text-white/90 max-w-[150px] sm:max-w-[200px] truncate">
                  {currentUser}
                </span>
                <button
                  onClick={handleLogout}
                  className="p-1 text-white/60 hover:text-white rounded-full hover:bg-white/10 transition-colors ml-1"
                  title="Log out"
                >
                  <MdLogout size={18} />
                </button>
              </div>
            ) : (
              <button
                onClick={() => setIsAuthOpen(true)}
                className="px-5 py-2 bg-white/15 hover:bg-white/25 backdrop-blur-md border border-white/25 rounded-full text-sm font-semibold text-white transition-all shadow-sm hover:scale-105 active:scale-95"
              >
                Sign In / Register
              </button>
            )}
          </div>
        </nav>

        {/* Hero Header */}
        <header className="text-center mb-8 animate-fade-in">
          <h1 className="text-5xl font-extrabold text-white tracking-tight drop-shadow-md mb-2">
            SkyPulse Weather
          </h1>
          <p className="text-lg text-white/70 font-medium">
            Real-time weather, 5-day forecasts & saved destinations
          </p>
        </header>

        {/* Search Bar */}
        <SearchBar 
          onSearch={fetchByCity} 
          onGpsSearch={handleGpsSearch} 
          gpsLoading={gpsLoading} 
        />

        {/* Saved Locations Bar (if user is authenticated) */}
        {currentUser && (
          <SavedLocations
            savedLocations={savedLocations}
            loading={savedLoading}
            onSelectLocation={fetchByCity}
            onRefresh={loadSavedLocations}
          />
        )}

        {/* Weather Content */}
        <div className="mt-8">
          {loading && <LoadingSpinner />}
          
          {error && !loading && (
            <ErrorMessage message={error} onRetry={handleRetry} />
          )}

          {!loading && !error && weather && (
            <div className="space-y-8 pb-12">
              <CurrentWeather
                data={weather}
                isLoggedIn={!!currentUser}
                isSaved={!!isCurrentCitySaved}
                onSaveLocation={handleSaveCurrentCity}
                savingLocation={savingLocation}
                onOpenAuth={() => setIsAuthOpen(true)}
              />
              <ForecastList data={forecast} />
            </div>
          )}
        </div>
      </div>

      {/* Auth Modal */}
      <AuthModal
        isOpen={isAuthOpen}
        onClose={() => setIsAuthOpen(false)}
        onAuthSuccess={handleAuthSuccess}
      />
    </div>
  );
}

export default App;
