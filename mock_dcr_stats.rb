require 'sinatra'

set :bind => '0.0.0.0',
	:port => 8090,
	:dump_errors => true,
	:logging => true

after "/*" do
	response['Content-type'] = 'application/json'
end

get "/api/v1/get_stats" do
	sleep 2
	%~{"id":1,"blocks":27105,"difficulty":476185.77116471,"networkhashps":"7964398679299","coinsupply":"245159236490129","pooledtx":0,"est_sbits":10.68477885,"prev_est_sbits":10.72869965,"est_sbits_min":9.41107603,"est_sbits_max":57.72394191,"ticketpoolvalue":589314,"fees":0,"max_fees":0,"btc_high":0.003202,"btc_low":0.00286,"btc_last":0.00297532,"prev_day":0.00316,"btc_volume":14602.03462433,"usd_price":449.999,"avg_sbits":26.0069395747541,"three_voters":499,"four_voters":2458,"five_voters":20053,"createdAt":"2016-02-09T23:36:01.489Z","updatedAt":"2016-05-11T17:42:46.330Z","average_time":296,"average_minutes":4,"average_seconds":56,"poolsize":43519,"sbits":26.44861686,"supply":21000000,"premine":1680000,"mined_before_pos":89401,"reward":31.19582664}~
end
