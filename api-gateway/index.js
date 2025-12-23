const express = require('express');
const cors = require('cors');
const axios = require('axios');

const app = express();
const PORT = 3000;

// Middleware
app.use(cors()); // Allow frontend to talk to us
app.use(express.json()); // Allow us to read JSON data

// CONFIG: Where is the Communication Server?
// We don't talk to Java directly. We talk to the "Relay" (Port 5000).
const COMM_SERVER_URL = 'http://localhost:5000';

// ROUTE: Create Order
app.post('/create-order', async (req, res) => {
    try {
        console.log("GATEWAY: Received order request from React...");
        
        // Forward the request to the Communication Server
        const response = await axios.post(`${COMM_SERVER_URL}/relay/create-order`, req.body);
        
        // Send the result back to React
        res.json(response.data);
        
    } catch (error) {
        console.error("GATEWAY ERROR:", error.message);
        res.status(500).json({ message: "Order failed at Gateway level" });
    }
});

// ==========================================
// ROUTE: REGISTER USER (Forward to Java)
// ==========================================
app.post('/register-user', async (req, res) => {
    try {
        console.log("GATEWAY: 👤 Forwarding User Registration to Java...");
        
        // Forward the data to Java's new User Controller
        const response = await axios.post('http://localhost:8080/api/users/register', req.body);
        
        console.log("GATEWAY: ✅ User Registered Successfully");
        res.json(response.data);
        
    } catch (error) {
        console.error("GATEWAY ERROR (Register):", error.message);
        res.status(500).json({ message: "Registration failed at Gateway" });
    }
});

// ROUTE: LOGIN USER
app.post('/login-user', async (req, res) => {
    try {
        console.log("GATEWAY: 🔑 Forwarding Login to Java...");
        const response = await axios.post('http://localhost:8080/api/users/login', req.body);
        res.json(response.data);
    } catch (error) {
        console.error("GATEWAY LOGIN ERROR:", error.response ? error.response.data : error.message);
        res.status(404).json({ message: "Login Failed. User not found." });
    }
});

// ROUTE: Get Restaurants (Forward to Java)
app.get('/restaurants', async (req, res) => {
    try {
        const response = await axios.get('http://localhost:8080/api/restaurants');
        res.json(response.data);
    } catch (error) {
        res.status(500).json({ message: "Error fetching restaurants" });
    }
});

// ROUTE: Get My Orders
app.get('/my-orders/:name', async (req, res) => {
    try {
        const userName = req.params.name;
        // Forward to Java
        const response = await axios.get(`http://localhost:8080/api/orders/user/${userName}`);
        res.json(response.data);
    } catch (error) {
        res.status(500).json({ message: "Error fetching orders" });
    }
});

app.post('/cancel-order/:id', async (req, res) => {
    try {
        const orderId = req.params.id;
        // Forward to Java Load Balancer (Port 8082)
        const response = await axios.post(`http://localhost:8082/api/orders/cancel/${orderId}`);
        res.json(response.data);
    } catch (error) {
        // Pass the error message from Java (e.g., "Time limit exceeded") to Frontend
        res.status(400).json({ message: error.response ? error.response.data : "Cancellation failed" });
    }
});

// ROUTE: Add Restaurant
app.post('/add-restaurant', async (req, res) => {
    try {
        const response = await axios.post('http://localhost:8082/api/restaurants/add', req.body);
        res.json(response.data);
    } catch (error) {
        res.status(500).json({ message: "Failed to add restaurant" });
    }
});

// ROUTE: Delete Restaurant
app.delete('/delete-restaurant/:id', async (req, res) => {
    try {
        await axios.delete(`http://localhost:8082/api/restaurants/delete/${req.params.id}`);
        res.json({ message: "Deleted" });
    } catch (error) {
        res.status(500).json({ message: "Delete failed" });
    }
});

// ROUTE: Toggle Status
app.put('/toggle-restaurant/:id', async (req, res) => {
    try {
        const response = await axios.put(`http://localhost:8082/api/restaurants/toggle-status/${req.params.id}`);
        res.json(response.data);
    } catch (error) {
        res.status(500).json({ message: "Toggle failed" });
    }
});
// Start the Server
app.listen(PORT, () => {
    console.log(`✅ API Gateway running on http://localhost:${PORT}`);
});