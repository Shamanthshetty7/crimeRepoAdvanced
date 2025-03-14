import React, { useState, useEffect } from 'react';
import { Line } from 'react-chartjs-2';
import { Select, MenuItem, FormControl, InputLabel, Typography, Box, Card, CardContent } from '@mui/material';
import { getAllReportByMonth } from '../service/DashboardService'; // Make sure this function is correctly implemented

// Import Chart.js components and register them
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';

// Registering Chart.js components to make them available
ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

const DashboardReportLineChart = () => {
  const [reportsData, setReportsData] = useState(null);
  const [selectedYear, setSelectedYear] = useState(new Date().getFullYear());
  const [availableYears, setAvailableYears] = useState([]);  // Example years

  const fetchAllReportByMonth = async (year) => {
    const reportCountByMonths = await getAllReportByMonth(year);
    if (reportCountByMonths.status) {
      let zeroCount = 0;
      Object.entries(reportCountByMonths?.data).forEach(([month, count]) => {
        if (count === 0) {
          zeroCount++;
        }
      });
      if (zeroCount === 12) {
        setReportsData(null);
      } else {
        const months = Object.keys(reportCountByMonths?.data);
        const counts = Object.values(reportCountByMonths?.data);
        setReportsData({ months, counts });
      }
    } else {
      setReportsData(null);
    }
  };

  const generateYears = () => {
    const currentYear = new Date().getFullYear();
    const years = [];
    for (let year = 2000; year <= currentYear; year++) {
      years.push(year);
    }
    setAvailableYears(years);
  };

  useEffect(()=>{
    generateYears();
  },[])

  useEffect(() => {
    fetchAllReportByMonth(selectedYear);
  }, [selectedYear]);

  const handleYearChange = (event) => {
    setSelectedYear(event.target.value);
  };

  const chartData = {
    labels: reportsData ? reportsData.months : [],  // Use months as labels
    datasets: [
      {
        label: 'Reports per Month',
        data: reportsData ? reportsData.counts : [],
        fill: false,
        borderColor: 'rgba(39, 113, 255, 1)',
        tension: 0.1,
        pointBackgroundColor: '#00FF00',
        pointBorderColor: '#00FF00',
        pointRadius: 5,
      },
    ],
  };

  return (
    <div className='linechart-component'>
      <Box display="flex" flexDirection="column" alignItems="center" justifyContent="center" p={3} bgcolor="#DDE6ED" minHeight="100vh">
        {/* Select Year */}
        <FormControl fullWidth sx={{ width: 200, mb: 3 }}>
          <InputLabel id="year-select-label" sx={{ color: 'black' }}>Select Year</InputLabel>
          <Select
            labelId="year-select-label"
            value={selectedYear}
            label="Select Year"
            onChange={handleYearChange}
            sx={{
              borderColor: '#00FF00',
              '& .MuiOutlinedInput-notchedOutline': {
                borderColor: '#00FF00',
              },
              '& .MuiInputLabel-root': {
                color: '#00FF00',
              },
              '&:hover .MuiOutlinedInput-notchedOutline': {
                borderColor: '#00FF00',
              },
              color: 'black', 
            }}
          >
            {availableYears.map((year) => (
              <MenuItem key={year} value={year}>
                {year}
              </MenuItem>
            ))}
          </Select>
        </FormControl>

       
        <Card sx={{ width: '100%', maxWidth: 700, borderRadius: '10px', boxShadow: 2, backgroundColor: '#6EACDA' }}>
          <CardContent>
           
            {reportsData === null ? (
              <Typography variant="h6" align="center" color="white" sx={{ mt: 5 }}>
                No reports available for {selectedYear}
              </Typography>
            ) : (
              <Line data={chartData} width={500} height={300} options={{ maintainAspectRatio: false }} />
            )}
          </CardContent>
        </Card>
      </Box>
    </div>
  );
};

export default DashboardReportLineChart;
