import React, { useState } from 'react';
import { MdClose, MdEmail, MdLock } from 'react-icons/md';
import { loginUser, registerUser } from '../api/weatherApi';

export function AuthModal({ isOpen, onClose, onAuthSuccess }) {
  const [isLogin, setIsLogin] = useState(true);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  if (!isOpen) return null;

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);

    if (!email || !password) {
      setError('Please fill in all fields');
      return;
    }
    if (password.length < 6) {
      setError('Password must be at least 6 characters long');
      return;
    }

    setLoading(true);
    try {
      let data;
      if (isLogin) {
        data = await loginUser(email, password);
      } else {
        data = await registerUser(email, password);
      }
      
      localStorage.setItem('skypulse_token', data.token);
      localStorage.setItem('skypulse_user', data.email);
      onAuthSuccess({ email: data.email, token: data.token });
      onClose();
    } catch (err) {
      const serverMessage = err.response?.data?.message || err.response?.data?.error;
      setError(serverMessage || err.message || (isLogin ? 'Login failed' : 'Registration failed'));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm animate-fade-in">
      <div className="relative w-full max-w-md p-8 bg-slate-900/90 border border-white/20 rounded-3xl shadow-2xl backdrop-blur-xl">
        {/* Close Button */}
        <button
          onClick={onClose}
          className="absolute top-5 right-5 p-2 text-white/60 hover:text-white rounded-full hover:bg-white/10 transition-colors"
          title="Close"
        >
          <MdClose size={22} />
        </button>

        {/* Modal Title & Tab Switcher */}
        <div className="text-center mb-6">
          <h2 className="text-3xl font-bold text-white mb-2">
            {isLogin ? 'Welcome Back' : 'Create Account'}
          </h2>
          <p className="text-white/60 text-sm">
            {isLogin
              ? 'Sign in to access your saved weather locations'
              : 'Register to save your favorite cities and quick access'}
          </p>
        </div>

        {/* Tab Buttons */}
        <div className="flex bg-white/10 p-1 rounded-xl mb-6">
          <button
            type="button"
            onClick={() => { setIsLogin(true); setError(null); }}
            className={`flex-1 py-2 text-sm font-semibold rounded-lg transition-all ${
              isLogin ? 'bg-blue-600 text-white shadow-md' : 'text-white/70 hover:text-white'
            }`}
          >
            Sign In
          </button>
          <button
            type="button"
            onClick={() => { setIsLogin(false); setError(null); }}
            className={`flex-1 py-2 text-sm font-semibold rounded-lg transition-all ${
              !isLogin ? 'bg-blue-600 text-white shadow-md' : 'text-white/70 hover:text-white'
            }`}
          >
            Register
          </button>
        </div>

        {/* Error Alert */}
        {error && (
          <div className="mb-4 p-3 bg-red-500/20 border border-red-500/30 rounded-xl text-red-200 text-sm text-center animate-fade-in">
            {error}
          </div>
        )}

        {/* Form */}
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-xs font-medium text-white/70 mb-1 ml-1">Email</label>
            <div className="relative flex items-center">
              <MdEmail className="absolute left-3 text-white/50" size={20} />
              <input
                type="email"
                required
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="name@example.com"
                className="w-full py-3 pl-10 pr-4 bg-white/5 border border-white/15 rounded-xl text-white placeholder-white/40 focus:outline-none focus:ring-2 focus:ring-blue-500 text-sm"
              />
            </div>
          </div>

          <div>
            <label className="block text-xs font-medium text-white/70 mb-1 ml-1">Password</label>
            <div className="relative flex items-center">
              <MdLock className="absolute left-3 text-white/50" size={20} />
              <input
                type="password"
                required
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="At least 6 characters"
                className="w-full py-3 pl-10 pr-4 bg-white/5 border border-white/15 rounded-xl text-white placeholder-white/40 focus:outline-none focus:ring-2 focus:ring-blue-500 text-sm"
              />
            </div>
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full mt-4 py-3 bg-blue-600 hover:bg-blue-500 text-white font-semibold rounded-xl transition-colors shadow-lg disabled:opacity-50 flex items-center justify-center space-x-2"
          >
            {loading ? (
              <div className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin" />
            ) : (
              <span>{isLogin ? 'Sign In' : 'Create Account'}</span>
            )}
          </button>
        </form>
      </div>
    </div>
  );
}
