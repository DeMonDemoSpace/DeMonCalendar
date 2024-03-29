## DeMonCalendar
**一个基于[SuperCalendar](https://github.com/MagicMashRoom/SuperCalendar)和[CalendarExaple](https://github.com/codbking/CalendarExaple)改造的日历控件，可显示农历日期，仿小米滑动列表周月切换。**

### 只使用日历

```xml
<com.demon.calendar.view.CalendarView
        android:id="@+id/cv"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:cv_day="Monday"
        app:cv_isLunar="true"
        app:cv_isShowToday="true"
        app:cv_theme="Default" />
```
#### 自定义属性说明

|属性|类型|说明|
|---|---|---|
|cmv_day|枚举|Monday：一周的第一天是星期一，Sunday：一周的第一天是星期天|  
|cmv_isLunar|boolean|是否显示农历|  
|cmv_isShowToday|boolean|是否显示今天按钮|  
|cmv_theme|枚举|日历的主题|  

#### 代码中使用
```java
calendarView = findViewById(R.id.cv);
        HashMap<String, String> markData = new HashMap<>();
        markData.put("2018-10-9", "0");
        markData.put("2018-10-19", "1");
        markData.put("2018-10-29", "0");
        markData.put("2018-10-10", "1");
        calendarView.setMarkData(markData);//绑定需要标记的日期
        calendarView.setOnDateListener(new OnDateListener() {
            @Override
            public void onDateChange(CalendarDate date) {
                //日期改变时回调
                Log.i(TAG, "onDateChange: " + date.toString());
            }
        });
        
        //calendarView.refreshMarkData(markData); 用于标记数据的更改和刷新
```
#### 效果
<img src="https://raw.githubusercontent.com/DeMonLiu623/DeMonCalendar/master/img/device-2018-10-29-163959.png" 
alt="" height="500" width="300">

### 使用带list的日历

```xml
<com.demon.calendar.view.CalendarMemoView
        android:id="@+id/cv"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:cmv_day="Sunday"
        app:cmv_isLunar="true"
        app:cmv_theme="Custom" />
```
#### 自定义属性说明

|属性|类型|说明|
|---|---|---|
|cmv_day|枚举|Monday：一周的第一天是星期一，Sunday：一周的第一天是星期天|  
|cmv_isLunar|boolean|是否显示农历|  
|cmv_isShowToday|boolean|是否显示今天按钮|  
|cmv_theme|枚举|日历的主题|   

#### 代码中使用
```java
 calendarView = findViewById(R.id.cv);
        HashMap<String, String> markData = new HashMap<>();
        markData.put("2018-10-9", "班");
        markData.put("2018-10-19", "休");
        markData.put("2018-10-29", "假");
        markData.put("2018-10-10", "班");
        calendarView.setMarkData(markData);//绑定需要标记的日期
        list.addAll(Arrays.asList(getResources().getStringArray(R.array.titles)));
        calendarView.setAdapter(new ExampleAdapter(this, list));//给列表绑定适配器
        calendarView.setOnDateListener(new OnDateListener() {
            @Override
            public void onDateChange(CalendarDate date) {
                //日期改变时回调
                Log.i(TAG, "onDateChange: " + date.toString());
                list.clear();
                list.addAll(Arrays.asList(getResources().getStringArray(R.array.items)));
                calendarView.adapter.notifyDataSetChanged();
            }
        });
        
        //calendarView.refreshMarkData(markData); 用于标记数据的更改和刷新
```

#### 效果
<img src="https://raw.githubusercontent.com/DeMonLiu623/DeMonCalendar/master/img/device-2018-10-29-163924.png" 
alt="" height="500" width="300"><img src="https://raw.githubusercontent.com/DeMonLiu623/DeMonCalendar/master/img/device-2018-10-29-163947.png" 
alt="" height="500" width="300">

### 其他
1. 修改控件的日历主题，图标等，可下载源码修改。
2. 编写日历主题请参考源码的```CustomDayView.java```和```DefaultDayView.java```。
3. 更多使用方法，请看源码，或者示例程序。

### 版本
1. v1.0初始版本。
2. v1.1修复一些bug。
3. v1.2修复周月滑动的默认日期的问题，现在日期改变只有onDateChange(CalendarDate date)回调方法。
4. v1.3使用public日期适配器calendarView.calendarAdapter用于刷新日历。
5. v1.4新增calendarView.refreshMarkData(markData)用于标记数据的更改和刷新
6. v1.5修复标记数据数据刷新的bug
7. v1.7修复选择日期的bug

### 更多
请在issues留言，定期回复。

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
