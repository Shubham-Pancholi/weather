import React from 'react';
import { formatTemperature, getWeatherIconUrl } from '../utils/formatters';
import { MdBookmark, MdRefresh } from 'react-icons/md';

export function SavedLocations({ savedLocations, loading, onSelectLocation, onRefresh }) {
  if (loading) {
    return (
      <div className="w-full max-w-2xl mx-auto mb-8 p-4 bg-white/5 backdrop-blur-md border border-white/10 rounded-2xl flex items-center justify-center space-x-3 text-white/60">
        <div className="w-4 h-4 border-2 border-white/60 border-t-transparent rounded-full animate-spin" />
        <span>Loading saved locations...</span>
      </div>
    );
  }

  if (!savedLocations || savedLocations.length === 0) {
    return null;
  }

  return (
    <div className="w-full max-w-2xl mx-auto mb-8 animate-fade-in">
      <div className="flex items-center justify-between mb-3 px-1">
        <div className="flex items-center space-x-2 text-white/80">
          <MdBookmark className="text-yellow-400" size={20} />
          <h3 className="text-sm font-semibold tracking-wide uppercase">Saved Locations</h3>
        </div>
        {onRefresh && (
          <button
            onClick={onRefresh}
            className="p-1.5 text-white/60 hover:text-white rounded-lg hover:bg-white/10 transition-colors"
            title="Refresh saved locations"
          >
            <MdRefresh size={18} />
          </button>
        )}
      </div>

      <div className="grid grid-cols-2 sm:grid-cols-3 gap-3">
        {savedLocations.map((loc, idx) => (
          <button
            key={`${loc.city}-${loc.country || idx}`}
            onClick={() => onSelectLocation(loc.city)}
            className="flex items-center justify-between p-3 bg-white/10 hover:bg-white/20 backdrop-blur-md border border-white/15 rounded-2xl transition-all hover:scale-[1.02] active:scale-[0.98] text-left group shadow-sm"
          >
            <div className="truncate mr-2">
              <p className="font-semibold text-white truncate text-sm group-hover:text-blue-300 transition-colors">
                {loc.city}
              </p>
              <p className="text-xs text-white/60 truncate capitalize">
                {loc.description || loc.country}
              </p>
            </div>
            
            <div className="flex items-center shrink-0">
              {loc.icon && (
                <img
                  src={getWeatherIconUrl(loc.icon)}
                  alt={loc.description || ''}
                  className="w-8 h-8"
                />
              )}
              <span className="font-bold text-white text-sm ml-1">
                {formatTemperature(loc.temperature)}
              </span>
            </div>
          </button>
        ))}
      </div>
    </div>
  );
}
