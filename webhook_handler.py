#!/usr/bin/python3

import os
import requests
import json

 


while True:
	print("Waiting for true value from Firebase...")
	try:
		lounge = requests.get("https://emergencyfireresponder-sooprb.firebaseio.com/LoungeRoomHome.json")
		print("Got lounge value")
	except:
		print("Internet connection failiure")
	try:
		entrance = requests.get("https://emergencyfireresponder-sooprb.firebaseio.com/EntranceGoogleHome.json")
		print("Got entrance value")
	except:
		print("Internet connection failiure")
	jsonFileL = json.loads(lounge.text)
	jsonFileE = json.loads(entrance.text)
	print(entrance.text)
	valL = jsonFileL['Clicked']
	valE = jsonFileE["Clicked"]
	#valE = 'true'
	if valL == "true":
		print("Playing File for Google Home Lounge Room")
		os.system("castnow --address 192.168.43.185 playfile2.mp3")
		jsonFileL['Clicked'] = 'False'
		dataDump = json.dumps(jsonFileL)
		d = requests.put("https://emergencyfireresponder-sooprb.firebaseio.com/LoungeRoomHome.json", dataDump)
		print("Succesfully put values")
	if valE == "true":
		print("Playing alert for Google Home Entrance Room")
		os.system("castnow --address 192.168.43.169 playfile.mp3")
		jsonFileE['Clicked'] = 'False'
		dataDump = json.dumps(jsonFileE)
		d = requests.put("https://emergencyfireresponder-sooprb.firebaseio.com/EntranceGoogleHome.json",dataDump)
		print("Succesfully put values")

