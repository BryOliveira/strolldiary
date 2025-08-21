'use client';
import { useEffect, useState } from 'react'; 

export default function TestComponent() {
  const [data, setData] = useState<string>('Loading...');
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchDate = async () => {
      try {
        const response = await fetch('http://localhost:8080/test');
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        const result = await response.text();
        setData(result);
        console.log('Backend respones:', result);
      } catch (err) {
        console.error('Error fetching data:', err);
        setError(err instanceof Error ? err.message : 'Unknown error');
      }
    };

    fetchDate();
  }, []);

  if (error) {
    return (
      <div>
        <p>Error, womp womp</p>
      </div>
    );
  }
  return (
    <div>
      <p>
        Backend says: {data}
      </p>
    </div>
  );
}