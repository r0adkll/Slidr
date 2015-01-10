Slidr
================
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.r0adkll/slidableactivity/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.r0adkll/slidableactivity)

Easily add slide-to-dismiss functionality to your Activity by calling `SlidableAttacher.attach(this)` in your `onCreate(..)` method. 

## Usage

An example usage:

	public class ExampleActivity extends <Activity|FragmentActivity|ActionBarActivity> {
		
		@Override
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			SlidableAttacher.attach(this);
			setContentView(R.layout.activity_example);
		}
		
	}
	
`SlidableAttacher.attach(...)` will return a `SlideLockInterface` which gives you access to two methods:

	SlideLockInterface.lock();
	SlideLockInterface.unlock();
	
These methods lock or unlock the slidable touch interface.
	
## Including in your project

Include this line in your gradle build file:

	compile 'com.r0adkll:slidableactivity:{latest_version}'
	
## Author

-	Drew Heavner, **[r0adkll](http://r0adkll.com)**

## License

	Copyright (c) 2014 Drew Heavner
	
	Licensed under the Apache License, Version 2.0 (the "License"); 
	you may not use this file except in compliance with the License. 
	You may obtain a copy of the License at
	
	http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, 
	software distributed under the License is distributed on an 
	"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
	either express or implied. See the License for the specific 
	language governing permissions and limitations under the License.
