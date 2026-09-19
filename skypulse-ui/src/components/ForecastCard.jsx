import React from 'react';
import { formatTemperature, formatTime, getWeatherIconUrl } from '../utils/formatters';
import { WiUmbrella } from 'react-icons/wi';

export function ForecastCard({ item }) {
  const probPrecip = Math.round((item.pop || 0) * 100);

  return (
    <div className="flex flex-col items-center min-w-[100px] p-4 bg-white/10 backdrop-blur-md border border-white/20 rounded-2xl shadow-lg snap-center hover:bg-white/20 transition-colors">
      <span className="text-sm font-medium text-white/80 whitespace-nowrap">
        {formatTime(item.dateTime)}
      </span>
      
      <img 
        src={getWeatherIconUrl(item.icon)} 
        alt={item.description}
        className="w-16 h-16 my-2 drop-shadow-md"
      />
      
      <span className="text-xl font-bold mb-1">
        {formatTemperature(item.temperature)}
      </span>
      
      {probPrecip > 0 && (
        <div className="flex items-center text-blue-300 text-xs mt-1">
          <WiUmbrella size={16} />
          <span>{probPrecip}%</span>
        </div>
      )}
    </div>
  );
}
