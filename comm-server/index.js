const express = require('express');
const cors = require('cors');
const axios = require('axios');

const app = express();
const PORT = 5000;

app.use(cors());
app.use(express.json());

// CONFIG
const JAVA_SERVICE_URL = 'http://localhost:8080';
//const DRIVER_SERVICE_URL = 'http://localhost:4000';
const DRIVER_SERVICE_URL = 'http://localhost:4002';

// ROUTE: Assign Driver FIRST, then Save Order
app.post('/relay/create-order', async (req, res) => {
    try {
        console.log("COMM SERVER: 🔄 Processing Order...");

        // 1. Ask Driver Service for a Driver
        console.log("   1. Finding a Driver...");
        const driverResponse = await axios.post(`${DRIVER_SERVICE_URL}/api/drivers`, { orderId: '000' });
        const assignedDriver = driverResponse.data.driverName;
        console.log(`      -> Driver Assigned: ${assignedDriver}`);

        // 2. Send EVERYTHING (Food + Driver) to Java to save
        console.log("   2. Saving Order to Java...");
        const javaPayload = {
            ...req.body,       // Food, Price, Customer
            driver: assignedDriver // Add the driver we just found
        };
        
        const javaResponse = await axios.post(`${JAVA_SERVICE_URL}/api/orders`, javaPayload);
        
        // 3. Reply to Frontend
        res.json({
            status: "SUCCESS",
            orderId: javaResponse.data.orderId,
            driver: assignedDriver
        });

    } catch (error) {
        console.error("COMM SERVER ERROR:", error.message);
        res.status(500).json({ error: "Failed to process order" });
    }
});

app.listen(PORT, () => {
    console.log(`🚀 Communication Server running on http://localhost:${PORT}`);
});