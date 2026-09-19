import React from 'react';
import { formatTemperature, formatTime, getWeatherIconUrl } from '../utils/formatters';
import { WiHumidity, WiStrongWind, WiBarometer, WiSunrise, WiSunset } from 'react-icons/wi';
import { MdVisibility, MdBookmark, MdBookmarkBorder } from 'react-icons/md';

export function CurrentWeather({ data, isLoggedIn, isSaved, onSaveLocation, savingLocation, onOpenAuth }) {
  if (!data) return null;

  const handleBookmarkClick = () => {
    if (!isLoggedIn) {
      if (onOpenAuth) onOpenAuth();
      return;
    }
    if (onSaveLocation && !isSaved) {
      onSaveLocation(data.city);
    }
  };

  return (
    <div className="w-full max-w-2xl mx-auto bg-white/10 backdrop-blur-md border border-white/20 rounded-3xl p-8 shadow-2xl animate-slide-up mb-8 relative">
      <div className="flex flex-col md:flex-row justify-between items-center mb-8">
        <div className="text-center md:text-left mb-6 md:mb-0 flex items-start space-x-3">
          <div>
            <div className="flex items-center space-x-3">
              <h2 className="text-4xl font-bold mb-1 tracking-tight">{data.city}</h2>
              <button
                onClick={handleBookmarkClick}
                disabled={savingLocation}
                className={`p-2 rounded-full transition-all ${
                  isSaved
                    ? 'text-yellow-400 bg-yellow-400/10'
                    : 'text-white/60 hover:text-white hover:bg-white/10'
                }`}
                title={
                  !isLoggedIn
                    ? 'Sign in to save this location'
                    : isSaved
                    ? 'Location saved'
                    : 'Save this location'
                }
              >
                {savingLocation ? (
                  <div className="w-5 h-5 border-2 border-white/60 border-t-transparent rounded-full animate-spin" />
                ) : isSaved ? (
                  <MdBookmark size={24} />
                ) : (
                  <MdBookmarkBorder size={24} />
                )}
              </button>
            </div>
            <p className="text-white/70 text-lg">
              {data.country}
            </p>
          </div>
        </div>
        
        <div className="flex flex-col items-center">
          <div className="flex items-center">
            <img 
              src={getWeatherIconUrl(data.icon)} 
              alt={data.description} 
              className="w-24 h-24 drop-shadow-lg"
            />
            <span className="text-7xl font-extrabold tracking-tighter ml-2">
              {formatTemperature(data.temperature)}
            </span>
          </div>
          <p className="text-xl font-medium capitalize text-white/90">
            {data.description}
          </p>
          <p className="text-white/70 mt-1">
            Feels like {formatTemperature(data.feelsLike)}
          </p>
        </div>
      </div>

      <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mt-8 pt-8 border-t border-white/10">
        <div className="flex flex-col items-center p-3 bg-white/5 rounded-2xl">
          <WiHumidity size={32} className="text-white/80 mb-1" />
          <span className="text-sm text-white/60">Humidity</span>
          <span className="font-semibold text-lg">{data.humidity}%</span>
        </div>
        
        <div className="flex flex-col items-center p-3 bg-white/5 rounded-2xl">
          <WiStrongWind size={32} className="text-white/80 mb-1" />
          <span className="text-sm text-white/60">Wind</span>
          <span className="font-semibold text-lg">{data.windSpeed} m/s</span>
        </div>
        
        <div className="flex flex-col items-center p-3 bg-white/5 rounded-2xl">
          <WiBarometer size={32} className="text-white/80 mb-1" />
          <span className="text-sm text-white/60">Pressure</span>
          <span className="font-semibold text-lg">{data.pressure} hPa</span>
        </div>
        
        <div className="flex flex-col items-center p-3 bg-white/5 rounded-2xl">
          <MdVisibility size={28} className="text-white/80 mb-1 mt-1" />
          <span className="text-sm text-white/60">Visibility</span>
          <span className="font-semibold text-lg">{(data.visibility / 1000).toFixed(1)} km</span>
        </div>
        
        <div className="flex flex-col items-center p-3 bg-white/5 rounded-2xl col-span-2 md:col-span-2">
           <div className="flex w-full justify-around">
             <div className="flex flex-col items-center">
                <WiSunrise size={32} className="text-white/80 mb-1" />
                <span className="text-sm text-white/60">Sunrise</span>
                <span className="font-semibold text-lg">{formatTime(data.sunrise)}</span>
             </div>
             <div className="flex flex-col items-center">
                <WiSunset size={32} className="text-white/80 mb-1" />
                <span className="text-sm text-white/60">Sunset</span>
                <span className="font-semibold text-lg">{formatTime(data.sunset)}</span>
             </div>
           </div>
        </div>
      </div>
    </div>
  );
}
