SlidableActivity
================
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.r0adkll/slidableactivity/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.r0adkll/slidableactivity)

Easily add slide-to-dismiss functionality to your Activity by extending the `SlidableActivity` or `SupportSidableActivity`.

## Usage

An example usage:

	public class ExampleActivity extends SlidableActivity {
		
		@Override
		public void onCreate(Bundle savedInstanceState){
			setContentView(R.layout.activity_example);
			super.onCreate(savedInstanceState);
			
			// ... Other setup stuffz
			
		}
		
	}
	
Another way to use this library is to use the experimental Attacher class:

    SlidableAttacher.attach(Activity activity);
    
to attach the slide-to-dismiss functionality to any given activity.	

You can lock and unlock the sliding controller using the following functions.

	public void lock(){...}
	
	public void unlock(){...}
	
## Including in your project

Include this line in your gradle build file:

	compile 'com.r0adkll:slidableactivity:+'
	
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
