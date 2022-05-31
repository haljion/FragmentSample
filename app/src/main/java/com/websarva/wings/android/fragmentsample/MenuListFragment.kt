package com.websarva.wings.android.fragmentsample

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleAdapter

class MenuListFragment : Fragment() {
    // 大画面かどうかの判定フラグ
    private var _isLayoutXLarge = true

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // 自分が所属するアクティビティからmenuThanksFrameを取得
        val menuThanksFrame = activity?.findViewById<View>(R.id.menuThanksFrame)
        // menuThanksFrameがnull(存在しない場合)
        if (menuThanksFrame == null) {
            // 画面判定フラグを通常画面とする
            _isLayoutXLarge = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // フラグメントで表示する画面をXMLファイルからインフレートする
        val view = inflater.inflate(R.layout.fragment_menu_list, container, false)
        // 画面部品ListViewを取得
        val lvMenu = view.findViewById<ListView>(R.id.lvMenu)

        // SimpleAdapter で使用するMutableListオブジェクトを用意
        val menuList: MutableList<MutableMap<String, String>> = mutableListOf()

        // から揚げ定食 のデータを格納するMapオブジェクトの用意とmenuListへのデータ登録
        var menu = mutableMapOf("name" to "から揚げ定食", "price" to "800円")
        menuList.add(menu)
        // ハンバーグ定食 のデータを格納するMapオブジェクトの用意とmenuListへのデータ登録
        menu = mutableMapOf("name" to "ハンバーグ定食", "price" to "850円")
        menuList.add(menu)

        // SimpleAdapter 第4引数fromデータ用の用意
        val from = arrayOf("name", "price")
        // SimpleAdapter 第5引数toデータ用の用意
        val to = intArrayOf(android.R.id.text1, android.R.id.text2)
        // SimpleAdapter を生成
        val adapter = SimpleAdapter(activity, menuList, android.R.layout.simple_list_item_2, from, to)

        // アダプタの登録
        lvMenu.adapter = adapter

        // リスナの登録
        lvMenu.onItemClickListener = ListItemClickListener()

        return view
    }

    private inner class ListItemClickListener: AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            // タップされた行のデータを取得 SimpleAdapterでは1行分のデータは MutableMap型！
            val item = parent.getItemAtPosition(position) as MutableMap<String,String>

            // 定食名と金額を取得
            val menuName = item["name"]
            val menuPrice = item["price"]

            // 引き続きデータをまとめて格納できるBundleオブジェクト生成
            val bundle = Bundle()
            // Bundleオブジェクトに引き継ぎデータを格納
            bundle.putString("menuName", menuName)
            bundle.putString("menuPrice", menuPrice)

            // 大画面の場合
            if (_isLayoutXLarge) {
                // フラグメントトランザクションの開始
                val transaction = fragmentManager?.beginTransaction()
                // 注文完了フラグメントを生成
                val menuThanksFragment = MenuThanksFragment()
                // 引き継ぎデータを注文完了フラグメントに格納
                menuThanksFragment.arguments = bundle
                // 生成した注文完了フラグメントをmenuThanksFrameレイアウト部品に追加
                transaction?.replace(R.id.menuThanksFrame, menuThanksFragment)
                // フラグメントトランザクションのコミット
                transaction?.commit()
            }
            // 通常画面の場合
            else {
                // インデントオブジェクトを作成
                val intent2MenuThanks = Intent(activity, MenuThanksActivity::class.java)
                // 第2画面に送るデータを格納 ここでは、Bundleオブジェクトとしてまとめて格納
                intent2MenuThanks.putExtras(bundle)
                // 第2画面の起動
                startActivity(intent2MenuThanks)
            }
        }
    }
}