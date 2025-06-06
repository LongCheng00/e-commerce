import { View, Text, Button } from '@tarojs/components'
import Taro, { useLoad } from '@tarojs/taro'
import React, { useState } from 'react'
import './index.scss'

interface Product {
  id: number;
  name: string;
  price: number,
  stock: number,
  buy: number,
}

export default function Index() {
  const [products, setProducts] = useState<Product[]>([])
  useLoad(() => {
    console.log('Page loaded.')
    getData()
  })

  const getData = () => {
    Taro.request({
      url: 'http://localhost:8088/products',
      method: 'GET',
      success: (res) => {
        console.log('data:', res)
        const data = res.data.map(m => {
          return { ...m, buy: 0 }
        })
        setProducts(data)
      }, fail: (error) => {
        console.error(error)
      }
    })
  }

  const plus = (p) => {
    if (p.buy < p.stock) {
      setProducts(prev => {
        return prev.map(m => {
          return (m.id === p.id) ? { ...m, buy: m.buy + 1 } : m
        })
      })
    }
    console.log('plus for:', p, products)
  }
  const minus = (p) => {
    if (p.buy > 0) {
      setProducts(prev => {
        return prev.map(m => {
          return (m.id === p.id) ? { ...m, buy: m.buy - 1 } : m
        })
      })
    }
    console.log('minus for:', p, products)
  }
  const Order = () => {
    const items = products.filter(f => f.buy > 0).map(m => {
      return {
        productId: m.id,
        quantity: m.buy,
      }
    })
    if (items.length < 1) {
      return
    }
    console.log('orders:', items)
    Taro.request({
      url: 'http://localhost:8088/orders',
      method: 'POST',
      data: { items },
      success: (res) => {
        console.log('data:', res)
        const data = res.data
        getData()
        Taro.showModal({
          title: 'Order Success',
          content: `OrderId:${data.orderId} \nTotalPrice:${data.totalPrice}`,
          showCancel: false,
          confirmText: 'ok',
          confirmColor: '#07C160',
          success: function (res) {
            if (res.confirm) {
              console.log('user confirm')
            } else if (res.cancel) {
              console.log('user cancel')
            }
          }
        })
       
      }, fail: (error) => {
        console.error(error)
        Taro.showToast({ title: '' + error, icon: 'error', duration: 3000 })
      }
    })
  }

  return (
    <View className='index'>
      {products.map(m => (
        <View key={m.id}>
          <View>{m.name}</View>
          <View>Price:{m.price}, Stock:{m.stock}</View>
          <View className='to-cart'>Buy:<Button onClick={() => plus(m)} >+</Button>{m.buy}<Button onClick={() => minus(m)}>-</Button></View>
        </View>
      ))}
      <View><Button onClick={() => Order()}>Order</Button></View>
    </View>
  )
}
