import React from 'react';

export function LoadingSpinner() {
  return (
    <div className="flex flex-col items-center justify-center py-12 animate-fade-in">
      <div className="w-16 h-16 border-4 border-white/20 border-t-white rounded-full animate-spin shadow-lg mb-6"></div>
      <p className="text-xl text-white/90 font-medium">Fetching weather data...</p>
    </div>
  );
}
