import React from 'react';
import { ForecastCard } from './ForecastCard';
import { formatDate } from '../utils/formatters';

export function ForecastList({ data }) {
  if (!data || !data.forecasts || data.forecasts.length === 0) return null;

  // Group by day using the dateTime field from our backend DTO
  const groupedForecast = data.forecasts.reduce((acc, item) => {
    const dateStr = formatDate(item.dateTime);
    if (!acc[dateStr]) {
      acc[dateStr] = [];
    }
    acc[dateStr].push(item);
    return acc;
  }, {});

  return (
    <div className="w-full max-w-2xl mx-auto animate-slide-up animation-delay-200">
      <h3 className="text-2xl font-bold mb-6 text-white/90">5-Day Forecast</h3>
      
      <div className="space-y-8">
        {Object.entries(groupedForecast).map(([date, items]) => (
          <div key={date} className="w-full">
            <h4 className="text-lg font-medium text-white/70 mb-3 ml-2">{date}</h4>
            <div className="flex overflow-x-auto gap-4 pb-4 snap-x snap-mandatory hide-scrollbar">
              {items.map((item, index) => (
                <ForecastCard key={item.dateTime || index} item={item} />
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
