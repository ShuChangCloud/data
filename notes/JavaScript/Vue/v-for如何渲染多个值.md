# 请问vue.js的v-for如何渲染多个值？

今天在学习vue.js的时候遇到了一个问题，折腾了很久，
问题比较小白，还请知道的大神指点指点，
譬如:
<tr v-for="x in test" v-for="y in test2">
<td>{{ x.arr1 }}</td>
<td>{{ y.arr2 }}</td>
</tr>
上面代码是报错的，请问如何达到类似效果呢？





```js
<ul id="example-1">
  <li v-for="(item,index) in items">
    {{ item.message }}--{{test[index]['name']}}
  </li>
</ul>
<script>
  var example1 = new Vue({
    el: '#example-1',
    data: {
      items: [
        {message: 'foo'},
        {message: 'Bar'}
      ],
      test: [
        {name: 'hello'},
        {name: "hi"},
      ]
    }
  })
</script>
```

