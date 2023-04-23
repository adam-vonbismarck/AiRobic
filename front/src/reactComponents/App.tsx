import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Home from "./pages/home";
import About from "./pages/about";
import NotFound from "./pages/NotFound";
import WorkoutCalendar from "./elements/calendar";
import Register from "./pages/register";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/about" element={<About />} />
        <Route path="/calendar" element={<WorkoutCalendar />} />
        <Route path="*" element={<NotFound />} />
        <Route path="/register" element={<Register />} />
      </Routes>
    </Router>
  );
}

export default App;
