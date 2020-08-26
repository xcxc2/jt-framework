(window.webpackJsonp=window.webpackJsonp||[]).push([[37],{343:function(t,s,e){"use strict";e.r(s);var a=e(33),n=Object(a.a)({},(function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("ContentSlotsDistributor",{attrs:{"slot-key":t.$parent.slotKey}},[e("h1",{attrs:{id:"组件顺序"}},[e("a",{staticClass:"header-anchor",attrs:{href:"#组件顺序"}},[t._v("#")]),t._v(" 组件顺序")]),t._v(" "),e("p",[t._v("从 "),e("a",{attrs:{href:"../basic"}},[t._v("协议入门")]),t._v(" 和 "),e("a",{attrs:{href:"../annotation-based-dev"}},[t._v("注解驱动开发")]),t._v(" 的文档不难看出以下问题：")]),t._v(" "),e("h2",{attrs:{id:"请求消息映射的实现"}},[e("a",{staticClass:"header-anchor",attrs:{href:"#请求消息映射的实现"}},[t._v("#")]),t._v(" 请求消息映射的实现")]),t._v(" "),e("p",[t._v("请求消息映射的实现方式：")]),t._v(" "),e("ul",[e("li",[t._v("手动实现并注册 "),e("code",[t._v("RequestMsgBodyConverter")])]),t._v(" "),e("li",[t._v("基于 "),e("code",[t._v("@Jt808ReqMsgBody")]),t._v(" 注解方式处理")]),t._v(" "),e("li",[t._v("内置了 "),e("code",[t._v("AuthRequestMsgBodyConverter")])])]),t._v(" "),e("h2",{attrs:{id:"请求消息的业务处理的实现"}},[e("a",{staticClass:"header-anchor",attrs:{href:"#请求消息的业务处理的实现"}},[t._v("#")]),t._v(" 请求消息的业务处理的实现")]),t._v(" "),e("p",[t._v("请求消息的业务处理实现方式：")]),t._v(" "),e("ul",[e("li",[t._v("手动实现并注册 "),e("code",[t._v("MsgHandler")]),t._v(" 接口")]),t._v(" "),e("li",[t._v("基于 "),e("code",[t._v("@Jt808RequestMsgHandler")]),t._v(" 注解方式处理")]),t._v(" "),e("li",[t._v("内置了 "),e("code",[t._v("AuthMsgHandler")])])]),t._v(" "),e("h2",{attrs:{id:"引出的问题"}},[e("a",{staticClass:"header-anchor",attrs:{href:"#引出的问题"}},[t._v("#")]),t._v(" 引出的问题")]),t._v(" "),e("p",[t._v("以 "),e("code",[t._v("将请求消息映射为实体类")]),t._v(" 的 "),e("code",[t._v("RequestMsgBodyConverter")]),t._v(" 为例，假如：")]),t._v(" "),e("ul",[e("li",[e("ol",[e("li",[e("code",[t._v("jt808.entity-scan.enable-builtin-entity = true")])])]),t._v(" "),e("ul",[e("li",[t._v("至少内置了鉴权消息的实体类")])])]),t._v(" "),e("li",[e("ol",{attrs:{start:"2"}},[e("li",[t._v("自定义并注册了 "),e("code",[t._v("RequestMsgBodyConverter")]),t._v(" 的实现类")])])]),t._v(" "),e("li",[e("ol",{attrs:{start:"3"}},[e("li",[t._v("同时扫描了 "),e("code",[t._v("@Jt808ReqMsgBody")]),t._v(" 修饰的请求消息体实体类")])])])]),t._v(" "),e("div",{staticClass:"custom-block danger"},[e("p",{staticClass:"custom-block-title"},[t._v("？？？")]),t._v(" "),e("p",[t._v("那么此时到底由谁去处理 "),e("code",[t._v("byte[] -> 请求消息体实体类")]),t._v(" 的映射功能呢？？？")])]),t._v(" "),e("ul",[e("li",[t._v("像处理器链一样逐个往下调用谁能处理就谁处理，否则直接抛到下游？\n"),e("ul",[e("li",[t._v("实现类多了有点头大，实现也有点麻烦。")]),t._v(" "),e("li",[t._v("所以此处不按这种方式处理")])])]),t._v(" "),e("li",[t._v("按优先级找一个组件来处理？\n"),e("ul",[e("li",[t._v("个人认为一种消息由一个组件来处理就够了")]),t._v(" "),e("li",[t._v("如果处理逻辑太复杂，可以在单个组件内调用其他专门的复杂逻辑处理流程")]),t._v(" "),e("li",[t._v("所以此处选择了这种优先级的处理方式，"),e("strong",[t._v("相同功能的组件只会按照优先级注册其仅注册一个")])])])])]),t._v(" "),e("p",[t._v("所以提供了一个 "),e("code",[t._v("io.github.hylexus.jt808.support.OrderedComponent")]),t._v(" 接口，来处理组件注册时的 "),e("code",[t._v("优先级(相互覆盖)")]),t._v(" 问题。")]),t._v(" "),e("h2",{attrs:{id:"orderedcomponent"}},[e("a",{staticClass:"header-anchor",attrs:{href:"#orderedcomponent"}},[t._v("#")]),t._v(" OrderedComponent")]),t._v(" "),e("p",[t._v("以下是 "),e("code",[t._v("OrderedComponent")]),t._v(" 接口的声明：")]),t._v(" "),e("div",{staticClass:"language-java extra-class"},[e("pre",{pre:!0,attrs:{class:"language-java"}},[e("code",[e("span",{pre:!0,attrs:{class:"token keyword"}},[t._v("public")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token keyword"}},[t._v("interface")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token class-name"}},[t._v("OrderedComponent")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("{")]),t._v("\n\n    "),e("span",{pre:!0,attrs:{class:"token keyword"}},[t._v("int")]),t._v(" DEFAULT_ORDER "),e("span",{pre:!0,attrs:{class:"token operator"}},[t._v("=")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token number"}},[t._v("0")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(";")]),t._v("\n\n    "),e("span",{pre:!0,attrs:{class:"token keyword"}},[t._v("int")]),t._v(" ANNOTATION_BASED_DEV_COMPONENT_ORDER "),e("span",{pre:!0,attrs:{class:"token operator"}},[t._v("=")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token number"}},[t._v("100")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(";")]),t._v("\n\n    "),e("span",{pre:!0,attrs:{class:"token keyword"}},[t._v("int")]),t._v(" BUILTIN_COMPONENT_ORDER "),e("span",{pre:!0,attrs:{class:"token operator"}},[t._v("=")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token number"}},[t._v("200")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(";")]),t._v("\n\n\n    "),e("span",{pre:!0,attrs:{class:"token keyword"}},[t._v("default")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token keyword"}},[t._v("int")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token function"}},[t._v("getOrder")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("{")]),t._v("\n        "),e("span",{pre:!0,attrs:{class:"token keyword"}},[t._v("return")]),t._v(" DEFAULT_ORDER"),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(";")]),t._v("\n    "),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("}")]),t._v("\n\n    "),e("span",{pre:!0,attrs:{class:"token keyword"}},[t._v("default")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token keyword"}},[t._v("boolean")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token function"}},[t._v("shouldBeReplacedBy")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),e("span",{pre:!0,attrs:{class:"token class-name"}},[t._v("OrderedComponent")]),t._v(" other"),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("{")]),t._v("\n        "),e("span",{pre:!0,attrs:{class:"token comment"}},[t._v("// 数字越小优先级越高")]),t._v("\n        "),e("span",{pre:!0,attrs:{class:"token comment"}},[t._v("// 数字小的覆盖数字大的")]),t._v("\n        "),e("span",{pre:!0,attrs:{class:"token keyword"}},[t._v("return")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token keyword"}},[t._v("this")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(".")]),e("span",{pre:!0,attrs:{class:"token function"}},[t._v("getOrder")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token operator"}},[t._v(">")]),t._v(" other"),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(".")]),e("span",{pre:!0,attrs:{class:"token function"}},[t._v("getOrder")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(";")]),t._v("\n    "),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("}")]),t._v("\n"),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("}")]),t._v("\n")])])]),e("p",[t._v("内置的 "),e("code",[t._v("OrderedComponent")]),t._v(" 实现类至少有以下几个：")]),t._v(" "),e("p",{},[e("img",{attrs:{src:t.$withBase("/img/builtin-ordered-component.png"),alt:"builtin-ordered-component"}})]),t._v(" "),e("div",{staticClass:"custom-block tip"},[e("p",{staticClass:"custom-block-title"},[t._v("由以上源码不难看出组件的优先级问题：")]),t._v(" "),e("p",[t._v("相同功能的组件只会 "),e("code",[t._v("按照优先级注册其仅注册一个")]),t._v(" ：")]),t._v(" "),e("ul",[e("li",[e("ol",[e("li",[t._v("手动实现并注册的 "),e("code",[t._v("MsgHandler")]),t._v(" 和 "),e("code",[t._v("RequestMsgBodyConverter")]),t._v(" 优先级最高")])])]),t._v(" "),e("li",[e("ol",{attrs:{start:"2"}},[e("li",[t._v("基于注解实现的 "),e("code",[t._v("MsgHandler")]),t._v(" 和 "),e("code",[t._v("RequestMsgBodyConverter")]),t._v(" 次之")])])]),t._v(" "),e("li",[e("ol",{attrs:{start:"3"}},[e("li",[t._v("内置组件的优先级最低")])])])])])])}),[],!1,null,null,null);s.default=n.exports}}]);