[![](https://jitpack.io/v/DeMonLiu623/DeMonCalendar.svg)](https://jitpack.io/#DeMonLiu623/DeMonCalendar)

# DeMonCalendar
**一个基于[SuperCalendar](https://github.com/MagicMashRoom/SuperCalendar)和[CalendarExaple](https://github.com/codbking/CalendarExaple)改造的自用日历控件。**

### 效果

<img src="https://raw.githubusercontent.com/DeMonLiu623/DeMonCalendar/master/img/device-2018-10-29-163924.png" 
alt="" height="500" width="300"><img src="https://raw.githubusercontent.com/DeMonLiu623/DeMonCalendar/master/img/device-2018-10-29-163947.png" 
alt="" height="500" width="300"><img src="https://raw.githubusercontent.com/DeMonLiu623/DeMonCalendar/master/img/device-2018-10-29-163959.png" 
alt="" height="500" width="300">

### 使用

#### 引入
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

```
dependencies {
	        implementation 'com.github.DeMonLiu623:DeMonCalendar:v1.0'
	}
```

#### 只使用日历

```xml
<com.demon.calendar.view.CalendarView
        android:id="@+id/cv"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:cv_day="Monday"
        app:cv_isLunar="false"
        app:cv_isShowToday="true"
        app:cv_theme="Default" />
```
>cv_day|枚举|Monday：一周的第一天是星期一，Sunday：一周的第一天是星期天|  
>cv_isLunar|boolean|是否显示农历|  
>cv_isShowToday|boolean|是否显示今天按钮|  
>cv_theme|枚举|日历的主题|  

```java
 calendarView = findViewById(R.id.cv);
        HashMap<String, String> markData = new HashMap<>();
        markData.put("2018-10-9", "0");
        markData.put("2018-10-19", "1");
        markData.put("2018-10-29", "0");
        markData.put("2018-10-10", "1");
        calendarView.setMarkData(markData);//设置标记日期
        calendarView.setOnDateListener(new OnDateListener() {
            @Override
            public void onSelectDate(CalendarDate date) {
             //选择日期触发
            }

            @Override
            public void onPageDateChange(CalendarDate date) {
              //滑动日历触发
            }
        });
    }
```


#### 使用带list的日历

```xml
<com.demon.calendar.view.CalendarMemoView
        android:id="@+id/cv"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:cmv_day="Sunday"
        app:cmv_isLunar="true"
        app:cmv_theme="Custom" />
```
>cmv_day|枚举|Monday：一周的第一天是星期一，Sunday：一周的第一天是星期天|  
>cmv_isLunar|boolean|是否显示农历|  
>cmv_isShowToday|boolean|是否显示今天按钮|  
>cmv_theme|枚举|日历的主题|  

```java
calendarView = findViewById(R.id.cv);
        HashMap<String, String> markData = new HashMap<>();
        markData.put("2018-10-9", "班");
        markData.put("2018-10-19", "休");
        markData.put("2018-10-29", "假");
        markData.put("2018-10-10", "班");
        calendarView.setMarkData(markData);
        list.addAll(Arrays.asList(getResources().getStringArray(R.array.titles)));
        calendarView.setAdapter(new ExampleAdapter(this, list));//设置RecycleView的Adapter
        calendarView.setOnDateListener(new OnDateListener() {
            @Override
            public void onSelectDate(CalendarDate date) {
            
            }

            @Override
            public void onPageDateChange(CalendarDate date) {
                list.clear();
                list.addAll(Arrays.asList(getResources().getStringArray(R.array.titles)));
                calendarView.adapter.notifyDataSetChanged();
            }
        });
```
#### 其他
请看源码，或者示例程序。

### BUG or 问题
请E-mail：757454343@qq.com 联系我。

### MIT License

```
MIT License

Copyright (c) 2018 DeMon

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

```