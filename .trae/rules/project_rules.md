# Campus Hub Project Rules

## Frontend Development
- The frontend is built with Vue 3, Vite, and Element Plus
- Available commands:
  - `npm run dev` - Start development server
  - `npm run build` - Build for production
  - `npm run preview` - Preview production build
- No specific lint command is configured in package.json
- All Vue components should follow composition API pattern
- Use Element Plus components for UI elements
- Store user state in Pinia stores
- Use Axios for API requests with the provided request utility

## Backend Development
- The backend is built with Spring Boot and MyBatis-Plus
- API base URL is configured in the frontend request utility
- JWT authentication is implemented
- User roles and permissions are managed through the backend

## Project Structure
- `/frontend` - Vue.js frontend application
- `/backend` - Spring Boot backend application
- Main layout is in `/frontend/src/layout/BasicLayout.vue`
- Components are in `/frontend/src/components`
- Views are in `/frontend/src/views`
- Router configuration is in `/frontend/src/router/index.js`
- Stores are in `/frontend/src/stores`