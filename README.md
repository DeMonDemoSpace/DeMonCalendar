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

|属性|类型|描述|
|:|:|:|
|cv_day|枚举|Monday：一周的第一天是星期一，Sunday：一周的第一天是星期天|
|cv_isLunar|boolean|是否显示农历|
|cv_isShowToday|boolean|是否显示今天按钮|
|cv_theme|枚举|日历的主题|

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
|属性|类型|描述|
|:|:|:|
|cmv_day|枚举|Monday：一周的第一天是星期一，Sunday：一周的第一天是星期天|
|cmv_isLunar|boolean|是否显示农历|
|cmv_isShowToday|boolean|是否显示今天按钮|
|cmv_theme|枚举|日历的主题|

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