import { useState } from 'react';

export function useGeolocation() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const getLocation = () => {
    return new Promise((resolve, reject) => {
      setLoading(true);
      setError(null);

      if (!navigator.geolocation) {
        const errorMsg = 'Geolocation is not supported by your browser';
        setError(errorMsg);
        setLoading(false);
        reject(new Error(errorMsg));
        return;
      }

      navigator.geolocation.getCurrentPosition(
        (position) => {
          setLoading(false);
          resolve({
            lat: position.coords.latitude,
            lon: position.coords.longitude,
          });
        },
        (error) => {
          let errorMsg = 'Failed to get location';
          switch (error.code) {
            case error.PERMISSION_DENIED:
              errorMsg = 'User denied the request for Geolocation.';
              break;
            case error.POSITION_UNAVAILABLE:
              errorMsg = 'Location information is unavailable.';
              break;
            case error.TIMEOUT:
              errorMsg = 'The request to get user location timed out.';
              break;
            default:
              errorMsg = 'An unknown error occurred.';
              break;
          }
          setError(errorMsg);
          setLoading(false);
          reject(new Error(errorMsg));
        }
      );
    });
  };

  return { getLocation, loading, error };
}
