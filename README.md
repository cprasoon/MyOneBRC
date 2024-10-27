# MyOneBRC
Attempting to write clean and simple 1BRC challenge code and optimize it, 

Original Challenge: https://github.com/gunnarmorling/1brc
Use same to get baseline results

Using date and duration to compute time taken

Current Timings
| Round/File | Baseline     | WeatherDataReader | WeatherDataReaderParallelStreams | WeatherDataReaderParallelStreamsNoChecks |
|------------|--------------|-------------------|----------------------------------|------------------------------------------|
| Round 01   | 00:03:48.916 |    00:03:54.386   |           00:02:23.759           |               00:02:20.548               |
| Round 02   | 00:03:55.360 |    00:03:46.228   |           00:02:20.304           |               00:02:20.009               |
| Round 03   | 00:04:04.212 |    00:03:50.118   |           00:02:19.361           |               00:02:15.222               |
| Average    | 00:03:56.163 |    00:03:50:244   |           00:02:21.141           |               00:02:18.593               |

Notes:
1. No crazy solutions
2. Simple and easy
3. No need to go out of the way and make some custom solution
4. Rely on available tools and APIs
