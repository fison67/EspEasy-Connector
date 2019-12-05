/**
 *  ESP Easy DTH (v.0.0.5)
 *
 *  Authors
 *   - fison67@nate.com
 *  Copyright 2018
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
  
import groovy.json.JsonSlurper

metadata {
	definition (name: "ESP Easy", namespace: "fison67", author: "fison67") {
        capability "Sensor"
        capability "Motion Sensor"
        capability "Temperature Measurement"
        capability "Relative Humidity Measurement"
        capability "Illuminance Measurement"
        capability "Carbon Dioxide Measurement"
        capability "Dust Sensor"
        capability "Color Temperature"
		capability "Dust Sensor" // fineDustLevel : PM 2.5   dustLevel : PM 10
        capability "Refresh"
        
		attribute "value1", "number"
		attribute "value2", "number"
		attribute "value3", "number"
		attribute "value4", "number"
		attribute "value5", "number"
		attribute "value6", "number"
        attribute "lastCheckinDate", "date"
        
        command "setData"
        command "timerLoop"
        command "setEspName"
	}

	simulator {
	}
    
	preferences {
		input name: "autorefresh", title:"Select a auto refresh" , type: "enum", required: true, options: ["ON", "OFF"], defaultValue: "OFF", description:"DTH Auto refresh ON/OFF"      
	}

	tiles(scale: 2) {
    	standardTile("status1_name", "device.status1_name", width: 3, height: 1) {
            state "val", label: '${currentValue}',  backgroundColor: "#ffffff"
        }
        standardTile("status2_name", "device.status2_name", width: 3, height: 1) {
            state "val", label: '${currentValue}',  backgroundColor: "#ffffff"
        }
    	valueTile("status1", "device.status1", width: 3, height: 3) {
            state("val", label:'${currentValue}', defaultState: true, 
            	backgroundColors:[
                    [value: 31, color: "#153591"],
                    [value: 44, color: "#1e9cbb"],
                    [value: 59, color: "#90d2a7"],
                    [value: 74, color: "#44b621"],
                    [value: 84, color: "#f1d801"],
                    [value: 95, color: "#d04e00"],
                    [value: 96, color: "#bc2323"]
                ]
            )
        }
        valueTile("status2", "device.status2", width: 3, height: 3) {
            state("val", label:'${currentValue}', defaultState: true, 
            	backgroundColors:[
                    [value: 31, color: "#153591"],
                    [value: 44, color: "#1e9cbb"],
                    [value: 59, color: "#90d2a7"],
                    [value: 74, color: "#44b621"],
                    [value: 84, color: "#f1d801"],
                    [value: 95, color: "#d04e00"],
                    [value: 96, color: "#bc2323"]
                ]
            )
        }
        standardTile("status3_name", "device.status3_name", width: 3, height: 1) {
            state "name", label: '${currentValue}',  backgroundColor: "#ffffff"
        }
        standardTile("status4_name", "device.status4_name", width: 3, height: 1) {
            state "name", label: '${currentValue}',  backgroundColor: "#ffffff"
        }
    	valueTile("status3", "device.status3", width: 3, height: 3) {
            state("val", label:'${currentValue}', defaultState: true, 
            	backgroundColors:[
                    [value: 31, color: "#153591"],
                    [value: 44, color: "#1e9cbb"],
                    [value: 59, color: "#90d2a7"],
                    [value: 74, color: "#44b621"],
                    [value: 84, color: "#f1d801"],
                    [value: 95, color: "#d04e00"],
                    [value: 96, color: "#bc2323"]
                ]
            )
        }
        valueTile("status4", "device.status4", width: 3, height: 3) {
            state("val", label:'${currentValue}', defaultState: true, 
            	backgroundColors:[
                    [value: 31, color: "#153591"],
                    [value: 44, color: "#1e9cbb"],
                    [value: 59, color: "#90d2a7"],
                    [value: 74, color: "#44b621"],
                    [value: 84, color: "#f1d801"],
                    [value: 95, color: "#d04e00"],
                    [value: 96, color: "#bc2323"]
                ]
            )
        }
        standardTile("status5_name", "device.status5_name", width: 3, height: 1) {
            state "name", label: '${currentValue}',  backgroundColor: "#ffffff"
        }
        standardTile("status6_name", "device.status6_name", width: 3, height: 1) {
            state "name", label: '${currentValue}',  backgroundColor: "#ffffff"
        }
    	valueTile("status5", "device.status5", width: 3, height: 3) {
            state("val", label:'${currentValue}', defaultState: true, 
            	backgroundColors:[
                    [value: 31, color: "#153591"],
                    [value: 44, color: "#1e9cbb"],
                    [value: 59, color: "#90d2a7"],
                    [value: 74, color: "#44b621"],
                    [value: 84, color: "#f1d801"],
                    [value: 95, color: "#d04e00"],
                    [value: 96, color: "#bc2323"]
                ]
            )
        }
        valueTile("status6", "device.status6", width: 3, height: 3) {
            state("val", label:'${currentValue}', defaultState: true, 
            	backgroundColors:[
                    [value: 31, color: "#153591"],
                    [value: 44, color: "#1e9cbb"],
                    [value: 59, color: "#90d2a7"],
                    [value: 74, color: "#44b621"],
                    [value: 84, color: "#f1d801"],
                    [value: 95, color: "#d04e00"],
                    [value: 96, color: "#bc2323"]
                ]
            )
        }
        valueTile("lastCheckinDate", "device.lastCheckinDate", width: 5, height: 1) {
            state "name", label: 'Last Updated : ${currentValue}',  backgroundColor: "#ffffff"
        }
       
       	main (["status1","status2"])
      	details(["status1_name","status2_name","status1","status2","status3_name","status4_name","status3","status4", "status5_name","status6_name","status5","status6", "lastCheckinDate"])
	}
}

// parse events into attributes
def parse(String description) {
    def msg = parseLanMessage(description)
    
    def desc = msg.headers.toString()
    
    def content
    def temp = msg.headers.toString().split(" ")
    for(def i=0; i<temp.length; i++){
    	if(temp[i].contains("demo.php")){
        	content = temp[i].split("\\?")[1]
            break
        }
    }
    
    Map<String, Integer> map = new HashMap<String, Integer>();
    for(final String entry : content.split("&")) {
        final String[] parts = entry.split("\\=");
        if(parts.length > 1){
       	 map.put(parts[0], parts[1]);
        }
    }
    
    log.debug map
    
    def name = map.valuename.toLowerCase()
    def value = map.value
	if(name == "temperature"){
        sendEvent(name:"temperature", value: value as double, unit: "C")
    }else if(name == "humidity"){
        sendEvent(name:"humidity", value: value as double, unit: "%")
    }else if(name == "lux"){
        sendEvent(name:"illuminance", value: value as int) 
    }else if(name == "ppm"){
        sendEvent(name:"carbonDioxide", value: value as double)
    }else if(name == "pm2.5"){
    	sendEvent(name:"fineDustLevel", value: value as int)
    }else if(name == "pm10"){
    	sendEvent(name:"dustLevel", value: value as int)
    }else if(name == "color temperature"){
    	sendEvent(name:"colorTemperature", value: value as int)
    }else if(name == "motion"){
    	sendEvent(name:"motion", value: (value as int) == 0 ? "inactive" : "active")
    }
    
    def count = state['unique_' + name]
    sendEvent(name: "value${count}", value: value, displayed: false)
    
    updateLastTime()
}

def setUrl(String url){
    log.debug "URL >> ${url}"
	state.address = url
    state.lastTime = new Date().getTime()
    
    state.timeSecond = 5
//    timerLoop()
	refresh()
}

def setEspName(name){
	log.debug "SetName >> ${name}"
	state._name = name
}

def setData(data){

	state._data = data
    
    try{
    
    	def count = 1
        data.each{item->
        
        //    if(item.TaskName == state._name){
            	item.each{ key,value->
                    if(key == "TaskValues"){
                        value.each{ obj ->
                        	key = ""
                            obj.each{ subKey, subValue ->
                            //	log.debug subKey + " >> " + subValue
                                if(subKey == "Name"){
                                    sendEvent(name: "status${count}_name", value: subValue)
                        			key = subValue
                                }else if(subKey == "Value"){
                                	state['unique_' + key.toLowerCase()] = count
                                	if(key.toLowerCase() == "temperature"){
    									sendEvent(name:"temperature", value: subValue, unit: "C")
                                    }else if(key.toLowerCase() == "humidity"){
    									sendEvent(name:"humidity", value: subValue, unit: "%")
                                    }else if(key.toLowerCase() == "lux"){
    									sendEvent(name:"illuminance", value: subValue)
                                    }else if(key.toLowerCase() == "ppm"){
    									sendEvent(name:"carbonDioxide", value: subValue)
                                    }else if(key.toLowerCase() == "pm2.5"){
                                        sendEvent(name:"fineDustLevel", value: subValue)
                                    }else if(key.toLowerCase() == "pm10"){
                                        sendEvent(name:"dustLevel", value: subValue)
                                    }else if(key.toLowerCase() == "color temperature"){
                                        sendEvent(name:"colorTemperature", value: subValue)
                                    }else if(key.toLowerCase() == "motion"){
                                        sendEvent(name:"motion", value: (subValue == 0 ? "inactive" : "active"))
                                    }
                                    sendEvent(name: "status${count}", value: subValue, displayed: false)
                                }
                            }
                            count += 1
                        }
                    }
                }
          //  }
        }

        updateLastTime()
    }catch(e){
    	log.error "Error!!! ${e}"
    }
}

def setAutoRefresh(mode){
    state.autorefresh = mode
}

def updated(){
	unschedule()
    state.autorefresh = settings.autorefresh
    
	if(state.autorefresh == "ON"){
    	timerLoop()
        log.debug "Auto Refresh ON"
    }else{
        log.debug "Auto Refresh OFF"
		refresh()
	}
}

def refresh(){
	getStatusOfESPEasy()
}

def timerLoop(){
	if(state.autorefresh == "ON"){
        getStatusOfESPEasy()    
        startTimer(state.timeSecond.toInteger(), timerLoop)
    }
}

def startTimer(seconds, function) {
    def now = new Date()
	def runTime = new Date(now.getTime() + (seconds * 1000))
	runOnce(runTime, function) // runIn isn't reliable, use runOnce instead
}

def callback(physicalgraph.device.HubResponse hubResponse){
	def msg, json, status
    try {
        msg = parseLanMessage(hubResponse.description)
        def jsonObj = msg.json
        setData(jsonObj.Sensors)
    } catch (e) {
        log.error "Exception caught while parsing data: " + e 
    }
}

def getStatusOfESPEasy() {
    try{
    	def timeGap = new Date().getTime() - Long.valueOf(state.lastTime)
        if(timeGap > 1000 * 60){
            log.warn "ESP Easy device is not connected..."
       //     sendEvent(name: "status1", value: -1)
        //    sendEvent(name: "status2", value: -1)
        //    sendEvent(name: "status3", value: -1)
        //    sendEvent(name: "status4", value: -1)
        }
		log.debug "Try to get data from ${state.address}"
        def options = [
            "method": "GET",
            "path": "/json",
            "headers": [
                "HOST": state.address + ":80",
                "Content-Type": "application/json"
            ]
        ]
        def myhubAction = new physicalgraph.device.HubAction(options, null, [callback: callback])
        sendHubCommand(myhubAction)
    }catch(e){
    	log.error "Error!!! ${e}"
    }
}

def updateLastTime(){
	def now = new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone)
    sendEvent(name: "lastCheckinDate", value: now, displayed:false)
}
