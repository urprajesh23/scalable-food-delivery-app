const express = require('express');
const app = express();
const PORT = 4000;

app.use(express.json());

// A simple list of fake drivers
const drivers = [
    { id: "D1", name: "Ramesh", status: "Available" },
    { id: "D2", name: "Suresh", status: "Busy" },
    { id: "D3", name: "Priya", status: "Available" }
];

// ROUTE: Assign a Driver
app.post('/api/drivers', (req, res) => {
    console.log("DRIVER SERVICE: 🚚 Received request to find driver for Order ID:", req.body.orderId);

    // Simple Logic: Just pick the first available driver
    const assignedDriver = drivers.find(d => d.status === "Available");

    if (assignedDriver) {
        console.log(`DRIVER SERVICE: ✅ Assigned ${assignedDriver.name}`);
        res.json({
            status: "SUCCESS",
            message: "Driver assigned successfully",
            driverName: assignedDriver.name,
            driverId: assignedDriver.id
        });
    } else {
        res.status(404).json({ status: "FAILED", message: "No drivers available" });
    }
});

app.listen(PORT, () => {
    console.log(`🚚 Driver Service running on http://localhost:${PORT}`);
});