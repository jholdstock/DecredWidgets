require 'sinatra'

set :bind => '0.0.0.0',
	:port => 8090,
	:dump_errors => true,
	:logging => true

after "/*" do
	response['Content-type'] = 'application/json'
end

get "/api/v1/get_stats" do
	sleep 1
	%~{"id":1,"blocks":28256,"difficulty":669076.15367342,"networkhashps":"10908296048625","coinsupply":"248470670015858","pooledtx":0,"est_sbits":16.3769173,"prev_est_sbits":16.3769173,"est_sbits_min":16.03789461,"est_sbits_max":91.30093232,"ticketpoolvalue":617545,"fees":0,"max_fees":0,"btc_high":0.00317744,"btc_low":0.002785,"btc_last":0.0029,"prev_day":0.00278572,"btc_volume":8574.04873511,"usd_price":454.099,"avg_sbits":26.7750206664516,"three_voters":520,"four_voters":2632,"five_voters":21009,"createdAt":"2016-02-09T23:36:01.489Z","updatedAt":"2016-05-15T12:29:31.545Z","average_time":295,"average_minutes":4,"average_seconds":55,"poolsize":43592,"sbits":40.73293322,"supply":21000000,"premine":1680000,"mined_before_pos":89401,"reward":31.19582664}~
end
