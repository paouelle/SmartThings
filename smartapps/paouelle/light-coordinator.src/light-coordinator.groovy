/**
 * 	Level Coordinator 
 *  Version 1.0.0 - 3/3/16
 *  Adapted from Michael Struck's Color Coordinator
 *  By Patrick Ouellet
 *
 *  1.0.0 - Initial release
 *
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
definition(
    name: "Light Coordinator",
    namespace: "paouelle",
    author: "Patrick Ouellet",
    description: "Ties multiple lights to one specific light's level settings",
	category: "Convenience",
	iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/ColorCoordinator/CC.png",
	iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/ColorCoordinator/CC@2x.png"
)

preferences {
    page name: "mainPage"
}

def mainPage() {
	dynamicPage(name: "mainPage", title: "", install: true, uninstall: true) {
		section("Master Light") {
			input "master", "capability.switchLevel", title: "Light"
		}
		section("Lights that follow the master on/off settings") {
			input "slaves2", "capability.switch", title: "Lights", multiple: true, required: false
		}
 		section("Lights that follow the master level settings") {
			input "slaves", "capability.switchLevel", title: "Lights", multiple: true, required: false
		}
        section([mobileOnly:true], "Options") {
			label(title: "Assign a name", required: false)
			href "pageAbout", title: "About ${textAppName()}", description: "Tap to get application version, license and instructions"
        }
	}
}

page(name: "pageAbout", title: "About ${textAppName()}") {
	section {
    	paragraph "${textVersion()}\n${textCopyright()}\n\n${textLicense()}\n"
	}
	section("Instructions") {
		paragraph textHelp()
	}
}

def installed() {   
	init() 
}

def updated(){
	unsubscribe()
    init()
}

def init() {
	subscribe(master, "switch", onOffHandler)
	subscribe(master, "level", levelevelHandler)
}

def onOffHandler(evt){
	if (master.currentValue("switch") == "on") {
    	slaves?.on()
        levelHandler(evt) // make sure the level matches the master
        slaves2?.on()
    } else {
		slaves?.off()
        slaves2?.off()
    }
}

def levelHandler(evt) {	
   	def dimLevel = master.currentValue("level")

    slaves?.setLevel(dimLevel as Integer)
}

//Version/Copyright/Information/Help

private def textAppName() {
	def text = "Level Coordinator"
}	

private def textVersion() {
    def text = "Version 1.0.0 (03/03/2016)"
}

private def textCopyright() {
    def text = "Copyright © 2016 Patrick Ouellet"
}

private def textLicense() {
    def text =
		"Licensed under the Apache License, Version 2.0 (the 'License'); "+
		"you may not use this file except in compliance with the License. "+
		"You may obtain a copy of the License at"+
		"\n\n"+
		"    http://www.apache.org/licenses/LICENSE-2.0"+
		"\n\n"+
		"Unless required by applicable law or agreed to in writing, software "+
		"distributed under the License is distributed on an 'AS IS' BASIS, "+
		"WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. "+
		"See the License for the specific language governing permissions and "+
		"limitations under the License."
}

private def textHelp() {
	def text =
    	"This application will allow you to control the settings of multiple lights with one control. " +
        "Simply choose a master control light, and then choose the lights that will follow the settings of the master, "+
        "including on/off conditions and level."
}