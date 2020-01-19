#!/usr/bin/python3

import os
import requests
import json

 


while True:
	print("Waiting for true value from Firebase...")
	lounge = requests.get("https://emergencyfireresponder-sooprb.firebaseio.com/LoungeRoomHome.json")
	entrance = requests.get("https://emergencyfirerespnder-sooprb.firebaseio.com/EntranceRoomHome.json")
	jsonFileL = json.loads(c.text)
	jsonFileE = json.loads(d.text
	valL  = jsonFile['Clicked']
	if valL == "true":
		print("Playing File for Google Home Lounge Room")
		os.system("castnow --address 192.168.43.185 playfile.mp3")
		jsonFileL['Clicked'] = 'False'
		dataDump = json.dumps(jsonFileL)
		d = requests.put("https://emergencyfireresponder-sooprb.firebaseio.com/LoungeRoomHome.json", dataDump)
		print("Succesfully put values")
	if valE == "true":
		print("Playing alert for Google Home Entrance Room")
		os.system("castnow --address playfile.mp3")
		jsonFileE['Clicked'] = 'False'
		dataDump = json.dumps(jsonFileE)
		d = requests.put("https://emergencyfireresponder-sooprb.firebaseio.com/EntranceRoomHome.json",dataDump)
		print("Succesfully put values")

