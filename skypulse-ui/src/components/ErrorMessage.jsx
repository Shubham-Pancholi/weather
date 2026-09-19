import React from 'react';
import { MdErrorOutline } from 'react-icons/md';

export function ErrorMessage({ message, onRetry }) {
  return (
    <div className="w-full max-w-2xl mx-auto bg-red-500/20 backdrop-blur-md border border-red-500/30 rounded-3xl p-8 flex flex-col items-center justify-center shadow-lg animate-fade-in">
      <MdErrorOutline size={48} className="text-red-300 mb-4" />
      <h3 className="text-xl font-bold text-white mb-2">Oops! Something went wrong</h3>
      <p className="text-red-200 mb-6 text-center">{message}</p>
      
      {onRetry && (
        <button
          onClick={onRetry}
          className="px-6 py-2 bg-red-500/40 hover:bg-red-500/60 border border-red-400/50 rounded-full transition-colors text-white font-medium"
        >
          Try Again
        </button>
      )}
    </div>
  );
}
