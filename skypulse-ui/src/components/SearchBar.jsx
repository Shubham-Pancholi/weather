import React, { useState } from 'react';
import { MdSearch, MdMyLocation } from 'react-icons/md';

export function SearchBar({ onSearch, onGpsSearch, gpsLoading }) {
  const [city, setCity] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    if (city.trim()) {
      onSearch(city.trim());
      setCity('');
    }
  };

  return (
    <div className="w-full max-w-2xl mx-auto mb-8 animate-fade-in">
      <form onSubmit={handleSubmit} className="relative flex items-center">
        <input
          type="text"
          value={city}
          onChange={(e) => setCity(e.target.value)}
          placeholder="Search for a city..."
          className="w-full py-4 pl-6 pr-24 bg-white/10 backdrop-blur-md border border-white/20 rounded-full text-white placeholder-white/60 focus:outline-none focus:ring-2 focus:ring-white/50 shadow-lg text-lg"
        />
        <div className="absolute right-2 flex items-center space-x-1">
          <button
            type="button"
            onClick={onGpsSearch}
            disabled={gpsLoading}
            className="p-3 text-white/80 hover:text-white hover:bg-white/10 rounded-full transition-colors disabled:opacity-50"
            title="Use current location"
          >
            {gpsLoading ? (
              <div className="w-6 h-6 border-2 border-white/80 border-t-transparent rounded-full animate-spin" />
            ) : (
              <MdMyLocation size={24} />
            )}
          </button>
          <button
            type="submit"
            className="p-3 text-white/80 hover:text-white hover:bg-white/10 rounded-full transition-colors"
            title="Search"
          >
            <MdSearch size={24} />
          </button>
        </div>
      </form>
    </div>
  );
}
