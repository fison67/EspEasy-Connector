/**
 *  ESP Easy Connector (v.0.0.1)
 *
 * MIT License
 *
 * Copyright (c) 2018 fison67@nate.com
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
 
import groovy.json.*

definition(
    name: "ESP Easy Connector",
    namespace: "fison67",
    author: "fison67",
    description: "A Connector between ESP Easy and ST",
    category: "My Apps",
    iconUrl: "https://www.shareicon.net/data/256x256/2016/01/19/705449_connection_512x512.png",
    iconX2Url: "https://www.shareicon.net/data/256x256/2016/01/19/705449_connection_512x512.png",
    iconX3Url: "https://www.shareicon.net/data/256x256/2016/01/19/705449_connection_512x512.png",
    oauth: true
)

preferences {
   page(name: "mainPage")
   page(name: "findPage")
   page(name: "addPage")
   page(name: "donePage")
}


def mainPage() {
	state.addedCountNow = 0
    
    state.findAddressLastNumber = 1
    
	dynamicPage(name: "mainPage", title: "Manage your ESP Easy Devices", nextPage: null, uninstall: true, install: true) {
        section("Configure"){
           href "findPage", title:"Find Devices & Add Automatically", description:""
           href "addPage", title:"Add Device Manually", description:""
        }
    }
}

def findPage(){
    state.mode = "auto"
    calculateNextAddress()
    getStatusOfESPEasy(state.findAddress, findDeviceCallback)
	dynamicPage(name:"findPage", title:"Finding....", refreshInterval:3) {
		section("Please wait while we find your ESP Easy devices. It will be added automatically.") {
            paragraph "Added Count : " + state.addedCountNow + ",  " + state.findingStatus
        }
	}
}

def addPage(){
	state.done = "adding..."
    state.mode = "manual"
	dynamicPage(name:"addPage", title:"Add Page", nextPage: "donePage") {
		section("Fill the blank") {
        	input "devAddress", "string", title: "Address of ESP Easy", required: true
        }
	}
}

def donePage(){
    def addr = settings.devAddress
    log.debug "Executing donePage Addr >> ${addr}"
    getStatusOfESPEasy(addr, findDeviceCallback)
	dynamicPage(name:"donePage", title:"Status", refreshInterval:3) {
		section("Please wait....") {
        	paragraph "Device is ${state.done}"
        }
	}
}


def installed() {
    log.debug "Installed with settings: ${settings}"

    initialize()
}

def updated() {
    log.debug "Updated with settings: ${settings}"

    // Unsubscribe from all events
    unsubscribe()
    // Subscribe to stuff
    initialize()
}

def initialize() {
	log.debug "initialize"
}

def calculateNextAddress(){
	def hub = location.hubs[0]
    
    def address = hub.localIP
    def tmpAddr = address.split("\\.")
    state.findAddressLastNumber = state.findAddressLastNumber.toInteger() + 1
    if(number <= 255){
		state.findAddress = tmpAddr[0] + "." + tmpAddr[1] + "." + tmpAddr[2] + "." + state.findAddressLastNumber
        state.findingStatus = "Finding..."
        return true
    }
    state.findingStatus = "Finish!!!"
    return false
}

def findDeviceCallback(physicalgraph.device.HubResponse hubResponse){
	def msg, status, json
    try {
        msg = parseLanMessage(hubResponse.description)
        
        def jsonObj = msg.json
        log.debug jsonObj
        
        jsonObj.Sensors.each{ item->
        	def name = item.TaskName
            
            def dni = "esp-connector-" + name.toLowerCase()
            if(!getChildDevice(dni)){
                try{
                    def childDevice = addChildDevice("fison67", "ESP Easy", dni, location.hubs[0].id, [
                        "label": name
                    ])
                    def addr = state.mode == "auto" ? state.findAddress : settings.devAddress
            		log.debug "Name >> " + name
                    childDevice.setUrl(addr)
                    childDevice.setEspName(name)
                    state.addedCountNow = (state.addedCountNow.toInteger() + 1)
                    log.debug "ADD >> ${name}, addr=${addr}"
                    state.done = "Finsihed"
                }catch(e){
                    log.error("ADD DEVICE Error!!! ${e}")
                }
            }
        }
        
        
        
	} catch (e) {
        log.error("Exception caught while parsing data: "+e);
    }
}

def getStatusOfESPEasy(address, _callback) {
	log.debug "Find Address >> ${address}"
    def options = [
     	"method": "GET",
        "path": "/json",
        "headers": [
        	"HOST": address + ":80",
            "Content-Type": "application/json"
        ]
    ]
    def myhubAction = new physicalgraph.device.HubAction(options, null, [callback: _callback])
    sendHubCommand(myhubAction)
}
