export const formatTemperature = (temp) => {
  if (temp === null || temp === undefined || isNaN(temp)) return '--°C';
  return `${Math.round(temp)}°C`;
};

export const formatDate = (dateTimeStr) => {
  if (!dateTimeStr) return '';
  const date = new Date(dateTimeStr);
  if (isNaN(date.getTime())) return '';
  return date.toLocaleDateString('en-US', {
    weekday: 'short',
    month: 'short',
    day: 'numeric'
  });
};

export const formatTime = (dateTimeStr) => {
  if (!dateTimeStr) return '--:--';
  const date = new Date(dateTimeStr);
  if (isNaN(date.getTime())) return '--:--';
  return date.toLocaleTimeString('en-US', {
    hour: 'numeric',
    minute: '2-digit',
    hour12: true
  });
};

export const getWeatherBackground = (description) => {
  if (!description) return 'from-blue-900 to-blue-950';
  
  const desc = description.toLowerCase();
  
  if (desc.includes('clear') || desc.includes('sunny')) {
    return 'from-orange-500 via-amber-600 to-orange-800';
  }
  if (desc.includes('cloud')) {
    return 'from-gray-600 via-slate-700 to-gray-900';
  }
  if (desc.includes('rain') || desc.includes('drizzle')) {
    return 'from-cyan-900 via-blue-900 to-slate-900';
  }
  if (desc.includes('snow')) {
    return 'from-blue-200 via-blue-400 to-slate-500';
  }
  if (desc.includes('thunderstorm')) {
    return 'from-purple-900 via-indigo-900 to-gray-900';
  }
  if (desc.includes('mist') || desc.includes('fog') || desc.includes('haze')) {
    return 'from-gray-400 via-gray-500 to-slate-700';
  }
  
  return 'from-blue-900 to-slate-900';
};

export const getWeatherIconUrl = (iconCode) => {
  if (!iconCode) return '';
  return `https://openweathermap.org/img/wn/${iconCode}@2x.png`;
};
